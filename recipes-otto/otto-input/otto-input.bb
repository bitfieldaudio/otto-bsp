SUMMARY = "OTTO i2c input pseudo driver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://otto-input.c \
		"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
TARGET_CC_ARCH += "${LDFLAGS}"

S = "${WORKDIR}"

FILES_${PN} += "/home/root/otto/otto-input \
"

do_compile() {
	${CC} otto-input.c -o otto-input
}

do_install() {
    install -d ${D}/home/root/otto
	install -m 0755 otto-input ${D}/home/root/otto/
}