DESCRIPTION = "An SWUpdate compound image. Based on the meta-swupdate-boards layer."

LICENSE = "CLOSED"

inherit swupdate

SRC_URI = "\
    file://flash_mcu.sh \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "otto-image-dev"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "otto-image-dev"

SWUPDATE_IMAGES_FSTYPES[otto-image] = ".ext4.gz"