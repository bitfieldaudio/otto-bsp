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

SRC_URI = "\
"


IMAGE_FEATURES += "read-only-rootfs"
IMAGE_FEATURES += "splash"
IMAGE_FEATURES += "convenience-aliases"

IMAGE_INSTALL += "kernel \
        kernel-modules \
        alsa-utils \
        openocd \
        linux-firmware-rpidistro-bcm43430 \
        linux-firmware-rpidistro-bcm43455 \
        otto-startup \
        otto-core \
        otto-core-data \
        otto-mcu-fw \
        fbcp-ili9341 \
        psplash \
        swupdate \
        swupdate-progress \
"
#rt-tests
#hwlatdetect
#bluez5
#i2c-tools

# Add Extra 1 GB for ???
IMAGE_ROOTFS_EXTRA_SPACE = "0"

LICENSE = "MIT"

addtask add_startup_modules after do_rootfs before do_image
do_add_startup_modules () {
	echo "i2c-dev" >> ${IMAGE_ROOTFS}/etc/modules
	echo "snd-seq-midi" >> ${IMAGE_ROOTFS}/etc/modules
	echo "uinput" >> ${IMAGE_ROOTFS}/etc/modules
  # TODO: This is probably the wrong place to do this in
  # No getty on ttyS0:
  sed -i '\|^ttyS0::respawn|d' ${IMAGE_ROOTFS}/etc/inittab 
  sed -i '\|^tty1::respawn|d' ${IMAGE_ROOTFS}/etc/inittab 
  # Autologin instead:
  cat >> "${IMAGE_ROOTFS}/etc/inittab" <<EOF
tty1::respawn:/bin/login -f root </dev/tty1 >/dev/tty1 2>&1 
ttyS0::respawn:/bin/login -f root </dev/ttyS0 >/dev/ttyS0 2>&1 
EOF
  install -d ${IMAGE_ROOTFS}/etc/rcS.d
  mkdir -p ${IMAGE_ROOTFS}/data
}

# Make sure the initial env is baked into the boot partition
DEPENDS += "u-boot-mkenvimage-native"

IMAGE_BOOT_FILES += "uboot.env"
IMAGE_PREPROCESS_COMMAND += "do_save_env;"
do_save_env () {
  mkenvimage -s 0x4000 -o ${DEPLOY_DIR_IMAGE}/uboot.env ${DEPLOY_DIR_IMAGE}/u-boot-initial-env
}

# Deploy the boot partition to be used in the .swu file
IMAGE_POSTPROCESS_COMMAND += "do_deploy_vfat_partition;"
do_deploy_vfat_partition () {
  install -m 0644 ${WORKDIR}/build-wic/sdimage-otto-*.p1 ${DEPLOY_DIR_IMAGE}/wic-boot-partition.vfat
}




# vim: ft=sh
