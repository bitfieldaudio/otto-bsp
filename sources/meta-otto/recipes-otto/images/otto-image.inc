require recipes-core/images/core-image-minimal.bb

# Skip processing of this recipe if linux-raspberrypi-rt is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-raspberrypi-rt":
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_virtual/kernel to linux-raspberrypi-rt to enable it")
}

DESCRIPTION = "OTTO Linux SD card image"

# qemuwrapper-cross is needed in DEPENDS to fix error with font cache update
DEPENDS = "linux-raspberrypi-rt qemuwrapper-cross"

IMAGE_FEATURES += "splash"

IMAGE_INSTALL += "kernel-modules \
				alsa-utils \
				openocd \
				linux-firmware-rpidistro-bcm43430 \
				linux-firmware-rpidistro-bcm43455 \
		    crda \
        otto-startup \
        otto-core \
        otto-core-data \
        otto-mcu-fw \
        fbcp-ili9341 \
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

# vim: ft=sh
