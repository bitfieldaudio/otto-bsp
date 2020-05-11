DESCRIPTION = "The OTTO core software."
HOMEPAGE = "https://github.com/topisani/OTTO"

#SRCREV = "a68fd2b6783ffee1a019512b12d7b796ceedf550"
SRC_URI = "git://github.com/OTTO-project/OTTO;branch=develop;rev=develop"
PV = "1.0.0+git${SRCPV}"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#DEPENDS = "virtual/libgles2 alsa-lib userland libcxx compiler-rt libunwind"
DEPENDS = "virtual/libgles2 alsa-lib userland valgrind libdbus-c++"
#TOOLCHAIN = "clang"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

# Allow cmake access to host utilities because it needs git
OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE += "-DOTTO_BOARD=rpi-proto-1"
# This flag is also propagated to CXXFLAGS
#TARGET_CFLAGS += "-Wno-psabi"

FILES_${PN} += " \
        /data/bin/otto \
        /data/bin/otto-tests \
        /data/data/* \
"

#INSTALL_PREFIX = "${@bb.utils.contains('PACKAGECONFIG', '')}"

do_install_append () {
	install -d ${D}/data
	install -d ${D}/data/bin
	install -m 0755 bin/otto ${D}/data/bin/otto
	install -m 0755 bin/test ${D}/data/bin/otto-tests
	install -d ${D}/data/data
	cp -r ${S}/data/* ${D}/data/data
}
