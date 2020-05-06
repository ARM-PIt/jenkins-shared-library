def call(Map dockerTagAndPushToRegistry) {
  if ("${REGISTRY_URL}" == 'https://docker.io') {
    withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS}", url: ""]) {
      def dockerImage = docker.build("${REGISTRY_PATH}/${IMAGE_NAME}", "--no-cache .")
      dockerImage.push("latest")
      dockerImage.push("${GIT_HASH_SHORT}")
      dockerImage.push("${IMAGE_PATCH}")
      dockerImage.push("${IMAGE_PATCH}-${BUILD_TYPE}.${BUILD_DATE}")
    }
  } else {
      withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS}", url: "${REGISTRY_URL}"]) {
        def dockerImage = docker.build("${REGISTRY_PATH}/${IMAGE_NAME}", "--no-cache .")
        dockerImage.push("latest")
        dockerImage.push("${GIT_HASH_SHORT}")
        dockerImage.push("${IMAGE_PATCH}")
        dockerImage.push("${IMAGE_PATCH}-${BUILD_TYPE}.${BUILD_DATE}")
      }
  }
}
