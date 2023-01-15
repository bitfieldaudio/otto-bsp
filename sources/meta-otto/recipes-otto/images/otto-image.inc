require recipes-core/images/core-image-minimal.bb
inherit otto-features

python () {
    kern = d.getVar("PREFERRED_PROVIDER_virtual/kernel")
    if kern != "linux-raspberrypi-rt":
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_virtual/kernel to linux-raspberrypi-rt to enable it. Value is {}".format(kern))
}

DESCRIPTION = "OTTO Linux SD card image"

# qemuwrapper-cross is needed in DEPENDS to fix error with font cache update
DEPENDS = "linux-raspberrypi-rt qemuwrapper-cross"

IMAGE_FEATURES += "read-only-rootfs"
IMAGE_FEATURES += "splash"
IMAGE_FEATURES += "convenience-aliases"

IMAGE_INSTALL += "\
  kernel \
  kernel-modules \
  linux-firmware-rpidistro-bcm43430 \
  linux-firmware-rpidistro-bcm43455 \
  fbcp-ili9341 \
  alsa-utils \
  openocd \
  psplash \
  swupdate \
  swupdate-progress \
  otto-core \
  otto-core-data \
  otto-mcu-fw \
"
# These are nice-to-haves
IMAGE_INSTALL += "rsyslog"

#rt-tests
#hwlatdetect
#bluez5
#i2c-tools

LICENSE = "MIT"

# Ensures the created environment image gets added to the boot partition
IMAGE_BOOT_FILES += "uboot.env"

# Deploy the boot partition to be used in the .swu file
IMAGE_POSTPROCESS_COMMAND += "do_deploy_vfat_partition;"
do_deploy_vfat_partition () {
  install -m 0644 ${WORKDIR}/build-wic/sdimage-otto-*.p1 ${DEPLOY_DIR_IMAGE}/wic-boot-partition.vfat
}

# vim: ft=sh
