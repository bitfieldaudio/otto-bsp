DESCRIPTION ?= "OTTO Overlay RootFS init script"
SUMMARY = "Init script for overlay rootfs"
HOMEPAGE = "https://github.com/OTTO-project/meta-otto"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

SRC_URI = "file://init \
		file://LICENSE \
		"

S = "${WORKDIR}"

# we don't need a compiler nor a c library for these files
INHIBIT_DEFAULT_DEPS = "1"

do_install() {
        install -m 0755 ${WORKDIR}/init ${D}/init
        install -d "${D}/media/rfs/ro"
        install -d "${D}/media/rfs/rw"
}

FILES_${PN} += "/init"
FILES_${PN} += "/media/rfs"
