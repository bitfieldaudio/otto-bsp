DESCRIPTION = "The OTTO core software."
HOMEPAGE = "https://github.com/topisani/OTTO"

SRCREV = "d4579024004cb6394371565e18ba0e23a40f19d0"
SRC_URI = "git://github.com/adorbs/OTTO;branch=adorbs-develop"
# SRC_URI = "git:///home/adorbs/OTTO/;protocol=file;branch=adorbs-develop"
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
				/home/root/otto/data/* \
"

do_install_append () {
	install -d ${D}/home/root/otto
	install -m 0755 bin/otto ${D}/home/root/otto/
	install -d ${D}/home/root/otto/data
	cp -r ${S}/data/* ${D}/home/root/otto/data/
}
