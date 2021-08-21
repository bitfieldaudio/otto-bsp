DESCRIPTION = "An SWUpdate compound image. Based on the meta-swupdate-boards layer."

LICENSE = "CLOSED"

inherit swupdate

SRC_URI = "\
    file://emmcsetup.lua \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "otto-image"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "otto-image"

SWUPDATE_IMAGES_FSTYPES[otto-image] = ".ext4.gz"