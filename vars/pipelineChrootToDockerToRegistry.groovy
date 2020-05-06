def call(Map pipelineChrootToDockerToRegistry) {
  pipeline {
    agent any
    environment {
      GIT_CREDENTIALS = "cedb6908-8b3d-49a3-9137-a086e65920c3"
      REGISTRY_CREDENTIALS = "0f4f74fc-ffde-476b-ba5b-777cde8455d5"
      REGISTRY_PATH = "${pipelineChrootToDockerToRegistry.registry_path}"
      REGISTRY_URL = "${pipelineChrootToDockerToRegistry.registry_url}"
      BUILD_TYPE = "${pipelineChrootToDockerToRegistry.build_type}"
      DISTRO = "${pipelineChrootToDockerToRegistry.distro}"
      IMAGE_NAME = "${pipelineChrootToDockerToRegistry.image_name}"
      APT_URL = "${pipelineChrootToDockerToRegistry.apt_url}"
      APT_KEY_URL = "${pipelineChrootToDockerToRegistry.apt_key_url}"
      APT_OPTIONS = "${pipelineChrootToDockerToRegistry.apt_options}"
      GIT_URL = "${pipelineChrootToDockerToRegistry.project_git_url}"
      GIT_BRANCH =  "${pipelineChrootToDockerToRegistry.project_git_branch}"
      GIT_CHECKOUT_DIR = "${pipelineChrootToDockerToRegistry.image_name}"
      RPIFW_GIT_CHECKOUT_DIR = "${pipelineChrootToDockerToRegistry.rpifw_git_checkout_dir}"
      RPIFW_GIT_URL = "${pipelineChrootToDockerToRegistry.rpifw_git_url}"
      RPIFW_GIT_BRANCH =  "${pipelineChrootToDockerToRegistry.rpifw_git_branch}"
      RPIUL_GIT_CHECKOUT_DIR = "${pipelineChrootToDockerToRegistry.rpiul_git_checkout_dir}"
      RPIUL_GIT_URL = "${pipelineChrootToDockerToRegistry.rpiul_git_url}"
      RPIUL_GIT_BRANCH =  "${pipelineChrootToDockerToRegistry.rpiul_git_branch}"
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
      stage('If Raspbian: Checkout Extras') {
        steps {
          script {
            if ("${DISTRO}" == 'raspbian') {
              gitCheckoutRaspbianExtras()
            } else if ("${DISTRO}" == 'debian') {
                echo "Debian build, no additional assets. Moving on."
            }
          }
        }
      }
      stage('Build Chroot') {
        steps {
          script {
            env.DOCKER_BUILD_DIR = "${WORKSPACE}/${GIT_CHECKOUT_DIR}"
            ws(DOCKER_BUILD_DIR) {
              chrootBuildImportDocker()
            }
          }
        }
      }
      stage('Tag and Push to Registry') {
        steps {
          script {
            dockerTagAndPushToRegistry()
          }
        }
      }
    }
    post {
      always {
        sh 'docker rmi ${IMAGE_NAME}'
        cleanWs()
      }
    }
  }
}
