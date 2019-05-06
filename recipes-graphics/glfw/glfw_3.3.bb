DESCRIPTION = "GLFW is an Open Source, multi-platform library for creating windows with OpenGL contexts and receiving input and events."
AUTHOR = "Camilla Berglund"
HOMEPAGE = "http://www.glfw.org/"
LICENSE = "Zlib | Libpng"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=90c6dd54408744b0f8a55f2a6c7ad870"

DEPENDS = "libpng libglu zlib libxi libxcursor libxinerama libxrandr"
REQUIRED_DISTRO_FEATURES = "x11"

inherit pkgconfig cmake distro_features_check

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "https://github.com/glfw/glfw/archive/${PV}/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "5be03812f5d109817e6558c3fab7bbe1"

PR = "r0"

S = "${WORKDIR}/${PN}-${PV}"

EXTRA_OECMAKE += "-DBUILD_SHARED_LIBS=ON -DLIB_SUFFIX=${@d.getVar('baselib', True).replace('lib', '')}"

FILES_${PN}-dev += "\
	${libdir}/cmake \
"