def call(Map gitCheckoutRaspbianExtras) {
  checkout([
    $class: 'GitSCM', 
    branches: [[name: "${RPIFW_GIT_BRANCH}"]], 
    doGenerateSubmoduleConfigurations: false, 
    extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${RPIFW_GIT_CHECKOUT_DIR}"], [$class: 'CloneOption', depth: 1, noTags: false, reference: '', shallow: true]],
    submoduleCfg: [], 
    userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIALS}", url: "${RPIFW_GIT_URL}"]]
  ])
  checkout([
    $class: 'GitSCM',
    branches: [[name: "${RPIUL_GIT_BRANCH}"]],
    doGenerateSubmoduleConfigurations: false,
    extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: "${RPIUL_GIT_CHECKOUT_DIR}"], [$class: 'CloneOption', depth: 1, noTags: false, reference: '', shallow: true]],
    submoduleCfg: [],
    userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIALS}", url: "${RPIUL_GIT_URL}"]]
  ])
}
