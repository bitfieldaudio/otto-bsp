# Add some aliases to make the shell more convenient to use
#
IMAGE_FEATURES[validitems] += "convenience-aliases"
ROOTFS_POSTPROCESS_COMMAND += '${@bb.utils.contains("IMAGE_FEATURES", "convenience-aliases", "convenience_aliases_hook; ", "", d)}'

convenience_aliases_hook() {
  echo "alias ls='ls --color=auto'" >> ${IMAGE_ROOTFS}${sysconfdir}/profile
  echo "alias ll='ls -al --color=auto'" >> ${IMAGE_ROOTFS}${sysconfdir}/profile
}