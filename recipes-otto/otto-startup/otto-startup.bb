DESCRIPTION ?= "OTTO Startup Script"
SUMMARY = "Startup Script for the OTTO Synthesizer"
HOMEPAGE = "https://github.com/adorbs/meta-otto"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

SRC_URI = "file://otto-startup.sh \
		file://LICENSE \
		"

S = "${WORKDIR}"

# we don't need a compiler nor a c library for these files
INHIBIT_DEFAULT_DEPS = "1"

FILES_${PN} = "/etc/profile.d/otto-startup.sh \
"

do_install() {
    install -d ${D}/etc/profile.d
    install -m 0755 otto-startup.sh ${D}/etc/profile.d/
}