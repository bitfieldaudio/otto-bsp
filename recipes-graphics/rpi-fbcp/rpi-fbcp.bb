# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=20cd56e60f87e0c4ddd2b6b2d6241e6f"

SRC_URI = "file://rpi-fbcp.zip \
		file://rpi-fbcp.sh \
		"

DEPENDS = "virtual/libgles2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

inherit pkgconfig cmake update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "${PN}.sh"
INITSCRIPT_PARAMS_${PN} = "start 8 5 . stop 21 0 1 6 ."

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""

do_install() {
    install -d ${D}${bindir}
    install -m 0755 fbcp ${D}${bindir}
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/rpi-fbcp.sh ${D}${sysconfdir}/init.d/
}