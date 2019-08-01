require recipes-core/images/core-image-minimal.bb

# Skip processing of this recipe if linux-raspberrypi-rt is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers.
python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-raspberrypi-rt":
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_virtual/kernel to linux-raspberrypi-rt to enable it")
}

DESCRIPTION = "A small image just capable of allowing a device to boot plus a \
real-time test suite and tools appropriate for real-time use."

# qemuwrapper-cross is needed in DEPENDS to fix error with font cache update
DEPENDS = "linux-raspberrypi-rt qemuwrapper-cross"

IMAGE_FEATURES += "splash"

IMAGE_INSTALL += "kernel-modules \
				rt-tests \
				hwlatdetect \
				rpi-fbcp \
				otto-core \
				alsa-utils \
				i2c-tools \
				otto-input \
				openocd \
				toot-mcu-fw \
				linux-firmware-rpidistro-bcm43430 \
				linux-firmware-rpidistro-bcm43455 \
				bluez-firmware-rpidistro-bcm43430a1-hcd \
				bluez-firmware-rpidistro-bcm4345c0-hcd \
				connman \
			    connman-client \
			    crda \
			    bluez5 \
			    openssh \
"

# Add Extra 100 MB for ???
IMAGE_ROOTFS_EXTRA_SPACE = "102400"

LICENSE = "MIT"

### If not using the fonts in the otto-core data dir, then add ttf-roboto to IMAGE_INSTALL
# addtask create_fonts_symlink after do_rootfs before do_image
# do_create_fonts_symlink () {
#	install -d ${IMAGE_ROOTFS}/home/root/otto/data/fonts
#	cd ${IMAGE_ROOTFS}/home/root/otto/data/fonts
#	ln -sf ../../../../..${datadir}/fonts/truetype/*.ttf ./
# }

addtask add_startup_modules after do_rootfs before do_image
do_add_startup_modules () {
	echo "i2c-dev" >> ${IMAGE_ROOTFS}/etc/modules
	echo "snd-seq-midi" >> ${IMAGE_ROOTFS}/etc/modules
	echo "uinput" >> ${IMAGE_ROOTFS}/etc/modules
}