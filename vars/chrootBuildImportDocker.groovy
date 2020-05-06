def call(Map chrootBuildImportDocker) {
  if ("${DISTRO}" == 'raspbian') {
    sh 'curl -L ${APT_KEY_URL} | gpg --import --no-default-keyring --keyring ./apt.gpg' 
    sh 'debootstrap --verbose --keyring ./apt.gpg --arch armhf --variant minbase --foreign buster chroot-build-base ${APT_URL}'
    sh 'chroot chroot-build-base /debootstrap/debootstrap --second-stage'
    sh 'echo "deb ${APT_URL} ${APT_OPTIONS}" > chroot-build-base/etc/apt/sources.list'
    sh 'chroot chroot-build-base apt-get update'
    sh 'chroot chroot-build-base apt-get -y upgrade'
    sh 'mkdir -p chroot-build-base/lib/modules/'
    sh 'cp -r ../rpi-firmware-essentials/modules/* chroot-build-base/lib/modules/'
    sh 'cp -r ../rpi-firmware-essentials/hardfp/opt/vc chroot-build-base/opt/'
    sh 'cp -r ../userland/interface/* chroot-build-base/usr/include/'
    sh 'echo "/opt/vc/lib" > chroot-build-base/etc/ld.so.conf.d/00-vmcs.conf'
    sh 'chroot chroot-build-base ln -s /opt/vc/bin/vcgencmd /usr/bin/vcgencmd'
    sh 'chroot chroot-build-base ldconfig'
    sh 'tar -C chroot-build-base -c . | docker import - ${IMAGE_NAME}'
  } else if ("${DISTRO}" == 'debian') {
      sh 'curl -L ${APT_KEY_URL} | gpg --import --no-default-keyring --keyring ./apt.gpg'
      sh 'debootstrap --verbose --keyring ./apt.gpg --arch armhf --variant minbase --foreign buster chroot-build-base ${APT_URL}'
      sh 'chroot chroot-build-base /debootstrap/debootstrap --second-stage'
      sh 'echo "deb ${APT_URL} ${APT_OPTIONS}" > chroot-build-base/etc/apt/sources.list'
      sh 'echo "APT::Install-Recommends "false";" > chroot-build-base/etc/apt/apt.conf.d/00-disable-recommends.conf'
      sh 'chroot chroot-build-base apt-get update'
      sh 'chroot chroot-build-base apt-get -y upgrade'
      sh 'tar -C chroot-build-base -c . | docker import - ${IMAGE_NAME}'
  }
}
