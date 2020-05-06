def call(Map dockerBuildTags) {
  sshagent(["${GIT_CREDENTIALS}"]) {
    sh 'git fetch --unshallow --tags --force'
  }
  env.IMAGE_PATCH = sshagent(["${GIT_CREDENTIALS}"]) {
    sh(script: "git describe --abbrev=0 --tags", returnStdout: true).trim()
  }
  env.GIT_HASH_SHORT = sshagent(["${GIT_CREDENTIALS}"]) {
    sh(script: "git log -1 --pretty=%h", returnStdout: true).trim()
  }
  def (major, minor, patch) = (env.IMAGE_PATCH).tokenize( '.' )
  env.IMAGE_MAJOR = major
  env.IMAGE_MINOR = major+'.'+minor
  def start_time = new Date()
  env.BUILD_DATE = new Date().format('yyyyMMdd')
}
