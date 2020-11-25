DESCRIPTION = "The OTTO C++ Preprocessor."
HOMEPAGE = "https://github.com/otto-project/ocpppp"

SRCREV = "cc06bc3eb2d09d462e1b880fe4a55fa5b1eec6af"
SRC_URI = "git://github.com/OTTO-project/ocpppp"
PV = "1.0.0+git${SRCPV}"

# TODO: License
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

DEPENDS = "clang"
#TOOLCHAIN = "clang"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DCPM_SOURCE_CACHE=${WORKDIR}/cpm"

do_install() {
	install -D -m 0755 ${B}/bin/ocpppp ${D}${bindir}/ocpppp
}

BBCLASSEXTEND = "native nativesdk"
