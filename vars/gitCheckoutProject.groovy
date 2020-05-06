def call(Map gitCheckoutProject) {
  checkout([
    $class: 'GitSCM', 
    branches: [[name: "${GIT_BRANCH}"]], 
    doGenerateSubmoduleConfigurations: false, 
    extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${GIT_CHECKOUT_DIR}"], [$class: 'CloneOption', depth: 1, noTags: false, reference: '', shallow: true]],
    submoduleCfg: [], 
    userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIALS}", url: "${GIT_URL}"]]
  ])
}
