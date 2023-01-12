require update-image.inc

# images to build before building swupdate image
IMAGE_DEPENDS = "otto-image-dev"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "wic-boot-partition otto-image-dev"

SWUPDATE_IMAGES_FSTYPES[otto-image-dev] = ".ext4.gz"
