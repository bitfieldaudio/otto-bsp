DESCRIPTION = "fbcp-ili9341 screen driver"  
LICENSE = "MIT"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/juj/fbcp-ili9341.git;protocol=https \
           file://0001-Added-support-for-ST7789V.patch \
           "
PV = "1.0+git${SRCPV}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e07269cd84249a454c5d152cf5176dd5"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

DEPENDS = "linux-firmware-rpidistro virtual/libgles2 virtual/egl userland"

inherit pkgconfig cmake update-rc.d

EXTRA_OECMAKE = "-DSPI_BUS_CLOCK_DIVISOR=6 -DST7789V=ON -DGPIO_TFT_DATA_CONTROL=-1 -DGPIO_TFT_BACKLIGHT=12 -DBACKLIGHT_CONTROL=ON"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "${PN}.sh"
INITSCRIPT_PARAMS_${PN} = "start 1 2 3 4 5 . stop 21 0 1 6 ."

do_configure_prepend() {  
  grep "target_link_libraries(fbcp-ili9341 vchostif)" ${S}/CMakeLists.txt || echo "target_link_libraries(fbcp-ili9341 vchostif)" >> ${S}/CMakeLists.txt
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 fbcp-ili9341 ${D}${bindir}
    install -d ${D}${sysconfdir}/init.d/
	  install -m 0755 ${FILE_DIRNAME}/files/fbcp-ili9341.sh ${D}${sysconfdir}/init.d/
}