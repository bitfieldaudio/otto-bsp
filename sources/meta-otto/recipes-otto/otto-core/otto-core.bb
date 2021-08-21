DESCRIPTION = "The OTTO core software."
HOMEPAGE = "https://github.com/topisani/OTTO"

SRCREV = "361c38fa5b4a7bccfc81bbe53bade72612c0d873"
#SRC_URI = "git://github.com/OTTO-project/OTTO;branch=develop;rev=develop"
PV = "1.0.0+git${SRCPV}"

LICENSE = "CC_BY-NC-SA_4.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84809983ca177c86f6b0091234e38722"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "virtual/libgles2 alsa-lib userland libexecinfo libskia boost"

S = "${WORKDIR}/git"

inherit cmake pkgconfig update-rc.d
# Do not remove debug symbols
#INHIBIT_PACKAGE_STRIP = "1"
#INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Allow cmake access to host utilities because it needs git
OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE += "-DOTTO_BOARD=${OTTO_BOARD}"
EXTRA_OECMAKE += "-DCPM_SOURCE_CACHE=${WORKDIR}/cpm-cache"
EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"
# This flag is also propagated to CXXFLAGS
TARGET_CFLAGS += "-Wno-psabi"

PACKAGES += "${PN}-tests"
PACKAGES += "${PN}-data"

FILES_${PN} += "/data/bin/otto"
FILES_${PN}-tests += "/data/bin/otto-tests"
FILES_${PN}-data += "/data/data/*"
#INSTALL_PREFIX = "${@bb.utils.contains('PACKAGECONFIG', '')}"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "otto-core.sh"
INITSCRIPT_PARAMS_${PN} = "start 100 2 3 4 5 . stop 1 0 1 6 ."

do_install () {
	install -d ${D}/data
	install -d ${D}/data/bin
	install -m 0755 bin/otto ${D}/data/bin/otto
	install -m 0755 bin/test ${D}/data/bin/otto-tests
	install -d ${D}/data/data
	cp -r ${S}/data/* ${D}/data/data

  install -d ${D}${sysconfdir}/init.d/
  install -m 0755 ${FILE_DIRNAME}/files/otto-core.sh ${D}${sysconfdir}/init.d/
}
