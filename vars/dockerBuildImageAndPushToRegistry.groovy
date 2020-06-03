def call(Map dockerTagAndPushToRegistry) {
  if (("${REGISTRY_URL}" == 'https://docker.io') && ("${DOCKER_DIGEST}")) {
    withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS}", url: ""]) {
      def dockerImage = docker.build("${REGISTRY_PATH}/${IMAGE_NAME}", "--no-cache --build-arg DOCKER_DIGEST .")
      dockerImage.push("latest")
      dockerImage.push("${GIT_HASH_SHORT}")
      dockerImage.push("${IMAGE_PATCH}")
      dockerImage.push("${IMAGE_PATCH}-${BUILD_TYPE}.${BUILD_DATE}")
    }
  } else if (("${REGISTRY_URL}" != 'https://docker.io') && ("${DOCKER_DIGEST}")) {
      withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS}", url: "${REGISTRY_URL}"]) {
        def dockerImage = docker.build("${REGISTRY_PATH}/${IMAGE_NAME}", "--no-cache --build-arg DOCKER_DIGEST .")
        dockerImage.push("latest")
        dockerImage.push("${GIT_HASH_SHORT}")
        dockerImage.push("${IMAGE_PATCH}")
        dockerImage.push("${IMAGE_PATCH}-${BUILD_TYPE}.${BUILD_DATE}")
      }
  } else if (("${REGISTRY_URL}" == 'https://docker.io') && ("${DOCKER_DIGEST}" == 'null')) {
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
