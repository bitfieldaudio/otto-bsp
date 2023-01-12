DESCRIPTION ?= "OTTO MCU Firmware"
SUMMARY = "MCU Firmware for the OTTO Synthesizer"
HOMEPAGE = "https://github.com/adorbs/otto-mcu-fw"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

SRC_URI = "https://github.com/OTTO-project/otto-mcu-fw/releases/download/${OTTO_FW_RELEASE}/otto-mcu-fw.elf;name=elf \
		file://load-mcu-fw.sh \
		file://openocd.cfg \
		file://LICENSE"
SRC_URI[elf.md5sum] = "a432cf1bef40a5791b37571bcc632218"
SRC_URI[elf.sha256sum] = "df061d7096922911c1768e50c539669aea53cfda2c0d33271db276b82a344bd2"

S = "${WORKDIR}"

# we don't need a compiler nor a c library for these files, but install uses objcopy
# INHIBIT_DEFAULT_DEPS = "1"

FILES_${PN} = "/home/root/${PN}/*"

do_install() {
    install -d ${D}/home/root/${PN}
    install -m 0755 otto-mcu-fw.elf ${D}/home/root/${PN}/
    install -m 0755 load-mcu-fw.sh ${D}/home/root/${PN}/
    install -m 0755 openocd.cfg ${D}/home/root/${PN}/
    sed -i "s/<SWCLK>/${OPENOCD_SWCLK_PIN}/g;s/<SWDIO>/${OPENOCD_SWDIO_PIN}/g;s/<SRST>/${OPENOCD_SRST_PIN}/g" \
      ${D}/home/root/${PN}/openocd.cfg
    sed -i "s/<MCU_TYPE>/${OPENOCD_MCU_TYPE}/g" ${D}/home/root/${PN}/load-mcu-fw.sh
}
