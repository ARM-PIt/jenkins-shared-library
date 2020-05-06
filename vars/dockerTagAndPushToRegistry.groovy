def call(Map dockerTagAndPushToRegistry) {
  if ("${REGISTRY_URL}" == 'https://docker.io') {
    withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS}", url: ""]) {
      sh "docker tag ${IMAGE_NAME} ${REGISTRY_PATH}/${IMAGE_NAME}:latest"
      sh "docker tag ${IMAGE_NAME} ${REGISTRY_PATH}/${IMAGE_NAME}:${GIT_HASH_SHORT}"
      sh "docker tag ${IMAGE_NAME} ${REGISTRY_PATH}/${IMAGE_NAME}:${BUILD_TYPE}.${BUILD_DATE}"
      sh "docker push ${REGISTRY_PATH}/${IMAGE_NAME}:latest"
      sh "docker push ${REGISTRY_PATH}/${IMAGE_NAME}:${GIT_HASH_SHORT}"
      sh "docker push ${REGISTRY_PATH}/${IMAGE_NAME}:${BUILD_TYPE}.${BUILD_DATE}"
    }
  } else {
      withDockerRegistry([credentialsId: "${REGISTRY_CREDENTIALS}", url: "${REGISTRY_URL}"]) {
        sh "docker tag ${IMAGE_NAME} ${REGISTRY_PATH}/${IMAGE_NAME}:latest"
        sh "docker tag ${IMAGE_NAME} ${REGISTRY_PATH}/${IMAGE_NAME}:${GIT_HASH_SHORT}"
        sh "docker tag ${IMAGE_NAME} ${REGISTRY_PATH}/${IMAGE_NAME}:${BUILD_TYPE}.${BUILD_DATE}"
        sh "docker push ${REGISTRY_PATH}/${IMAGE_NAME}:latest"
        sh "docker push ${REGISTRY_PATH}/${IMAGE_NAME}:${GIT_HASH_SHORT}"
        sh "docker push ${REGISTRY_PATH}/${IMAGE_NAME}:${BUILD_TYPE}.${BUILD_DATE}"
      }
  }
}
