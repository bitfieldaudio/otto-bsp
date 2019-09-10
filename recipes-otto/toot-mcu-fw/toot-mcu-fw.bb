DESCRIPTION ?= "TOOT MCU Firmware"
SUMMARY = "MCU Firmware for the TOOT Synthesizer"
HOMEPAGE = "https://github.com/adorbs/toot-mcu-fw"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

SRC_URI = "https://github.com/adorbs/toot-mcu-fw/releases/download/v0.0.3/toot-mcu-fw.elf;name=elf \
		file://load-toot-mcu-fw.sh \
		file://openocd.cfg \
		file://LICENSE"
SRC_URI[elf.md5sum] = "3cc5245be23070cd04a9c474b280b05b"

S = "${WORKDIR}"

# we don't need a compiler nor a c library for these files, but install uses objcopy
# INHIBIT_DEFAULT_DEPS = "1"

FILES_${PN} = "/home/root/${PN}/* \
"

do_install() {
    install -d ${D}/home/root/${PN}
    install -m 0755 toot-mcu-fw.elf ${D}/home/root/${PN}/
    install -m 0755 load-toot-mcu-fw.sh ${D}/home/root/${PN}/
    install -m 0755 openocd.cfg ${D}/home/root/${PN}/
}
