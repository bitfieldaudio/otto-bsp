DESCRIPTION = "The OTTO-MCU communicator service."
HOMEPAGE = "https://github.com/bitfieldaudio/otto-mcu-communicator"

# SRCREV = "deb75f3e0742189e3c09a059fbf3115e62f0612e"
SRC_URI = "git://github.com/bitfieldaudio/otto-mcu-communicator;branch=master;rev=master"
PV = "1.0.0+git${SRCPV}"

LICENSE = "CC_BY-NC-SA_4.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84809983ca177c86f6b0091234e38722"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "libexecinfo boost"

S = "${WORKDIR}/git"

inherit cmake pkgconfig update-rc.d
# Do not remove debug symbols
#INHIBIT_PACKAGE_STRIP = "1"
#INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Allow cmake access to host utilities because it needs git
OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE += "-DCPM_SOURCE_CACHE=${WORKDIR}/cpm-cache"
EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"
# This flag is also propagated to CXXFLAGS
TARGET_CFLAGS += "-Wno-psabi"

#INSTALL_PREFIX = "${@bb.utils.contains('PACKAGECONFIG', '')}"
FILES_${PN} += "${COMMUNICATOR_INSTALL_DIR}"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "otto-mcu-communicator.sh"
INITSCRIPT_PARAMS_${PN} = "start 100 2 3 4 5 . stop 1 0 1 6 ."

COMMUNICATOR_INSTALL_DIR = "/home/root/otto-mcu-communicator"

do_install () {
	# Install service in proper location
	install -d ${D}${COMMUNICATOR_INSTALL_DIR}
	install -m 0755 bin/mcu_service ${D}${COMMUNICATOR_INSTALL_DIR}/mcu_service

	# Install init script
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${FILE_DIRNAME}/files/otto-mcu-communicator.sh ${D}${sysconfdir}/init.d/
}
