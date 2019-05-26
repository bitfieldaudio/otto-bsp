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
				ttf-roboto \
"

LICENSE = "MIT"

addtask create_fonts_symlink after do_rootfs before do_image
do_create_fonts_symlink () {
	install -d ${IMAGE_ROOTFS}/home/root/otto/data/fonts
	cd ${IMAGE_ROOTFS}/home/root/otto/data/fonts
	ln -sf ../../../../..${datadir}/fonts/truetype/*.ttf ./
}