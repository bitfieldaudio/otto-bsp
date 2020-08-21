DESCRIPTION ?= "FBCP for Raspberry Pi"
SUMMARY = "Framebuffer Copy application"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=20cd56e60f87e0c4ddd2b6b2d6241e6f"

SRC_URI = "file://rpi-fbcp.zip \
		file://rpi-fbcp.sh \
		"

DEPENDS = "virtual/libgles2 userland"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

inherit pkgconfig cmake update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "${PN}.sh"
INITSCRIPT_PARAMS_${PN} = "start 1 2 3 4 5 . stop 21 0 1 6 ."

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""

do_install() {
  install -d ${D}${bindir}
  install -m 0755 fbcp ${D}${bindir}
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/rpi-fbcp.sh ${D}${sysconfdir}/init.d/
}
