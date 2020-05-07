# Base config for OTTO machines

DEFAULTTUNE ?= "cortexa7thf-neon-vfpv4"

require conf/machine/include/tune-cortexa7.inc
require conf/machine/include/rpi-base.inc

IMAGE_FSTYPES = "tar.bz2 ext3 wic"
WKS_FILE = "sdimage-otto.wks"

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

RPI_KERNEL_DEVICETREE_OVERLAYS += "\
    overlays/fe-pi-audio.dtbo \
    overlays/r61505w.dtbo \
"

SPLASH = "psplash"

SDIMG_KERNELIMAGE ?= "kernel7.img"
UBOOT_MACHINE = "rpi_3_32b_config"
SERIAL_CONSOLES ?= "115200;ttyS0"
PREFERRED_PROVIDER_virtual/kernel = "linux-raspberrypi-rt"
PREFERRED_PROVIDER_virtual/egl = "userland"
PREFERRED_PROVIDER_virtual/libgles2 = "userland"
MACHINE_FEATURES_remove = "vc4graphics"
MACHINEOVERRIDES = "rpi:${MACHINE}"

# vim: ft=ini
