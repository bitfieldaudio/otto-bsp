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
DEPENDS = "linux-raspberrypi-rt"

IMAGE_FEATURES += "splash"

IMAGE_INSTALL += "kernel-modules \
				rt-tests \
				hwlatdetect \
				rpi-fbcp \
				otto-core \
"

LICENSE = "MIT"
