DESCRIPTION = "The OTTO core software."
HOMEPAGE = "https://github.com/bitfieldaudio/otto"

SRC_URI = "git://github.com/bitfieldaudio/otto;branch=develop;rev=develop \
	file://find-otto-core-exe.sh \
	file://midi-autoconnect.sh \
	file://otto-core.sh \
"

# Set this in distro conf
SRCREV ?= ""
PV = "1.0.0+git${SRCPV}"

LICENSE = "CC_BY-NC-SA_4.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84809983ca177c86f6b0091234e38722"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "virtual/libgles2 alsa-lib userland libexecinfo libskia boost"

RDEPENDS_${PN} += "otto-mcu-communicator"

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
PACKAGES += "${PN}-scripts"

OTTO_INSTALL_DIR = "/home/root/otto"

FILES_${PN} += "${OTTO_INSTALL_DIR}/bin/otto"
FILES_${PN}-tests += "${OTTO_INSTALL_DIR}/bin/otto-tests"
FILES_${PN}-data += "/data/data/*"
FILES_${PN}-data += "/data/resources/*"
FILES_${PN}-data += "/data/overrides"
FILES_${PN}-scripts += "/home/root/scripts/*"

RDEPENDS_${PN}-tests += "${PN}"

RDEPENDS_${PN} += "${PN}-data"
RDEPENDS_${PN} += "${PN}-scripts"
#INSTALL_PREFIX = "${@bb.utils.contains('PACKAGECONFIG', '')}"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "otto-core.sh"
INITSCRIPT_PARAMS_${PN} = "start 100 2 3 4 5 . stop 1 0 1 6 ."

do_install () {
	# Install otto in proper location
	install -d ${D}${OTTO_INSTALL_DIR}/bin
	install -m 0755 bin/otto ${D}${OTTO_INSTALL_DIR}/bin/otto
	install -m 0755 bin/test ${D}${OTTO_INSTALL_DIR}/bin/otto-tests
	
	# Install data and resources
	install -d ${D}/data/data
	cp -r ${S}/data/* ${D}/data/data
	install -d ${D}/data/resources
	cp -r ${S}/resources/* ${D}/data/resources

	# Prepare an overrides folder for adding otto override.
	# To use, copy over directories:
	# - /data/overrides/otto/bin
	# - /data/overrides/otto/data
	install -d ${D}/data/overrides
	# Install script to find appropriate executable
	install -d ${D}/home/root/scripts
	install -m 0755 ${FILE_DIRNAME}/files/find-otto-core-exe.sh ${D}/home/root/scripts/find-otto-core-exe.sh
	# Script for autoconnecting USB midi devices
	install -m 0755 ${FILE_DIRNAME}/files/midi-autoconnect.sh ${D}/home/root/scripts/midi-autoconnect.sh
	# Install init script
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${FILE_DIRNAME}/files/otto-core.sh ${D}${sysconfdir}/init.d/
}
