DESCRIPTION = "The OTTO core software."
HOMEPAGE = "https://github.com/topisani/OTTO"

SRCREV = "d9a7afd87dfb0f03b37ccc1c09f6e59fa57c0fdd"
SRC_URI = "git://github.com/topisani/OTTO;branch=develop"
PV = "1.0.0+git${SRCPV}"

LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9741c346eef56131163e13b9db1241b3"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "glfw alsa-lib userland"
TOOLCHAIN = "clang"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += "-DOTTO_BOARD=rpi-proto-1 -DOTTO_USE_LIBCXX=ON"

FILES_${PN} += "/home/root/otto/otto \
				/home/root/otto/test \
"

do_install_append () {
	install -d ${D}/home/root/otto
	install -m 0755 bin/otto ${D}/home/root/otto/
	install -m 0755 bin/test ${D}/home/root/otto/
}
