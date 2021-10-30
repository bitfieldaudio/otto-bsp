DESCRIPTION = "An SWUpdate compound image. Based on the meta-swupdate-boards layer."

LICENSE = "CLOSED"

SRC_URI = "\
    file://delete_overrides.sh \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "otto-image-dev"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "wic-boot-partition otto-image-dev"

SWUPDATE_IMAGES_FSTYPES[wic-boot-partition] = ".vfat"
SWUPDATE_IMAGES_FSTYPES[otto-image-dev] = ".ext4.gz"

SWU_FS_FILE = "${IMAGE_DEPENDS}-${MACHINE}.ext4.gz"
SWU_UBOOT_FILE = "wic-boot-partition.vfat"

inherit swupdate