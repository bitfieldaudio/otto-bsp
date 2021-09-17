DESCRIPTION = "An SWUpdate compound image. Based on the meta-swupdate-boards layer."

LICENSE = "CLOSED"

SRC_URI = "\
    file://flash_mcu.sh \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "otto-image"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "otto-image"

SWUPDATE_IMAGES_FSTYPES[otto-image] = ".ext4.gz"

SWU_FS_FILE = "${IMAGE_DEPENDS}-${MACHINE}.ext4.gz"
SWU_UBOOT_FILE = "u-boot.bin"

inherit swupdate