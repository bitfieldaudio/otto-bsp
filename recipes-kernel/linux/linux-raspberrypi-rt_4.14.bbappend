FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "\
	file://defconfig \
	file://0001-add-ili9320-dtoverlay.patch \
	file://0002-add-r61505w-and-hx8347h-drivers.patch \
"

COMPATIBLE_MACHINE_otto-proto-v1 = "otto-proto-v1"
