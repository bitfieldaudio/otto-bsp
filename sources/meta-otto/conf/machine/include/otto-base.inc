# Base config for OTTO machines

DEFAULTTUNE ?= "cortexa7thf-neon-vfpv4"

IMAGE_FSTYPES = "wic ext4.gz"
WKS_FILE = "sdimage-otto.wks"
WIC_CREATE_EXTRA_ARGS = "--no-fstab-update"

require conf/machine/include/tune-cortexa7.inc
require conf/machine/include/rpi-base.inc

MACHINE_EXTRA_RRECOMMENDS += "\
    linux-firmware-rpidistro-bcm43430 \
    linux-firmware-rpidistro-bcm43455 \
    bluez-firmware-rpidistro-bcm43430a1-hcd \
    bluez-firmware-rpidistro-bcm4345c0-hcd \
"

# Getting rid of misc junk
RPI_KERNEL_DEVICETREE_OVERLAYS_remove = "\
    overlays/at86rf233.dtbo \
    overlays/lirc-rpi.dtbo \
    overlays/rpi-poe.dtbo \
    overlays/vc4-kms-v3d.dtbo \
"

KERNEL_IMAGEDEST = "boot"
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = "${KERNEL_PACKAGE_NAME}-image"
IMAGE_BOOT_FILES ?= "${BOOTFILES_DIR_NAME}/* \
                 ${@make_dtb_boot_files(d)} \
                 u-boot.bin;${SDIMG_KERNELIMAGE} boot.scr \
                 "


RPI_KERNEL_DEVICETREE_OVERLAYS = ""

SPLASH = "psplash"

SDIMG_KERNELIMAGE ?= "kernel7.img"
UBOOT_MACHINE = "rpi_3_32b_config"

# Disable gettys on tty1 and serial consoles
# Login shells are started from otto-image.inc
SERIAL_CONSOLES = ""
USE_VT = ""
SYSVINIT_ENABLED_GETTYS = ""

PREFERRED_PROVIDER_virtual/kernel = "linux-raspberrypi"
PREFERRED_PROVIDER_virtual/egl = "userland"
PREFERRED_PROVIDER_virtual/libgles2 = "userland"
MACHINE_FEATURES_remove = "vc4graphics"
MACHINEOVERRIDES = "rpi:${MACHINE}"

# We use U-boot to load the kernel, since this lets us use SWUpdate
RPI_USE_U_BOOT = "1"

# vim: ft=ini
