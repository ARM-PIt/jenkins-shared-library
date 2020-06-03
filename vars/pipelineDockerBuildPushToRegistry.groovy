def call(Map pipelineDockerBuildPushToRegistry) {
  pipeline {
    agent any
    environment {
      GIT_CREDENTIALS = "cedb6908-8b3d-49a3-9137-a086e65920c3"
      REGISTRY_CREDENTIALS = "0f4f74fc-ffde-476b-ba5b-777cde8455d5"
      REGISTRY_PATH = "${pipelineDockerBuildPushToRegistry.registry_path}"
      REGISTRY_URL = "${pipelineDockerBuildPushToRegistry.registry_url}"
      BUILD_TYPE = "${pipelineDockerBuildPushToRegistry.build_type}"
      IMAGE_NAME = "${pipelineDockerBuildPushToRegistry.image_name}"
      GIT_URL = "${pipelineDockerBuildPushToRegistry.project_git_url}"
      GIT_BRANCH =  "${pipelineDockerBuildPushToRegistry.project_git_branch}"
      GIT_CHECKOUT_DIR = "${pipelineDockerBuildPushToRegistry.image_name}"
      DOCKER_DIGEST = "${pipelineDockerBuildPushToRegistry.docker_digest}"
    }
    stages {
      stage('Clear Workspace') {
        steps {
          cleanWs()
        }
      }
      stage('Checkout Project') {
        steps {
          script {
            gitCheckoutProject()
          }
        }
      }
      stage('Build Docker Tags') {
        steps {
          script {
            env.DOCKER_BUILD_DIR = "${WORKSPACE}/${GIT_CHECKOUT_DIR}"
            ws(DOCKER_BUILD_DIR) {
              dockerBuildTags()
            }
            echo "${IMAGE_PATCH}-${BUILD_TYPE}.${BUILD_DATE}"
            currentBuild.displayName = "Build: ${BUILD_ID} - Commit ID: ${GIT_HASH_SHORT} - Version: ${IMAGE_PATCH} - Image: ${REGISTRY_PATH}:${IMAGE_PATCH}-${BUILD_TYPE}.${BUILD_DATE}"
          }
        }
      }
      stage('Build Docker Image and Push to Registry') {
        steps {
          script {
            env.DOCKER_BUILD_DIR = "${WORKSPACE}/${GIT_CHECKOUT_DIR}"
            ws(DOCKER_BUILD_DIR) {
              dockerBuildImageAndPushToRegistry()
            }
          }
        }
      }
    }
    post {
      always {
        sh 'docker rmi ${REGISTRY_PATH}/${IMAGE_NAME}'
        cleanWs()
      }
    }
  }
}
