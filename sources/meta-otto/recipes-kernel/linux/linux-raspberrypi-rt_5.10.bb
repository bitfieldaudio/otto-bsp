LINUX_VERSION ?= "5.10.52"
LINUX_RPI_BRANCH ?= "rpi-5.10.y"
LINUX_RPI_KMETA_BRANCH ?= "yocto-5.10"

SRCREV_machine = "6495fa3396fb387d3871ced42a30281fe7be5352"
SRCREV_meta = "a19886b00ea7d874fdd60d8e3435894bb16e6434"

KMETA = "kernel-meta"

SRC_URI = "\
    git://github.com/raspberrypi/linux.git;name=machine;branch=${LINUX_RPI_BRANCH} \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${LINUX_RPI_KMETA_BRANCH};destsuffix=${KMETA} \
    file://0001-linux-5.10.59-rt51-fixed.patch \
    file://0002-usb-dwc_otg-fix-system-lockup-when-interrupts-are-threaded.patch \
    file://0003-Add-device-tree-overlay-for-st7789v.patch \
    file://defconfig \
"

ARM_KEEP_OABI = "0"

require recipes-kernel/linux/linux-raspberrypi.inc

PV = "${LINUX_VERSION}"

KERNEL_DTC_FLAGS += "-@ -H epapr"

# Load these kernel modules on boot
KERNEL_MODULE_AUTOLOAD += "i2c-dev"
KERNEL_MODULE_AUTOLOAD += "snd-seq-midi"
KERNEL_MODULE_AUTOLOAD += "uinput"

# We can't place the kernel in /boot, since we mount the boot partition there
KERNEL_IMAGEDEST = "boot-kernel"
