# jenkins-shared-library
Shared Libraries for Jenkins Pipelines

This repository contains libraries for building and maintaing ARM-PIt/armpits Docker Hub public images.

How to enable/start using in the Jenkins server: [https://jenkins.io/doc/book/pipeline/shared-libraries/](https://jenkins.io/doc/book/pipeline/shared-libraries/)

Functions
------------------
* Build Docker image from project Dockerfile
* Build Docker image from chroot
* Build Docker tags (git hash, date, semantic versioning)
* Git checkout project
* Git checkout additional assets for Raspbian

Pipelines
------------------
* Build from Dockerfile and send to registry
* Build chroot, import to Docker, send image to registry
