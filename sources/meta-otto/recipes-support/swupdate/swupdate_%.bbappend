FILESEXTRAPATHS_append := "${THISDIR}/${PN}:"

PACKAGECONFIG_CONFARGS = ""

SRC_URI += " \
    file://09-swupdate-args \
    file://swupdate.cfg \
    file://defconfig \
    "

do_install_append() {
    install -m 0644 ${WORKDIR}/09-swupdate-args ${D}${libdir}/swupdate/conf.d/

    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/swupdate.cfg ${D}${sysconfdir}

    # hwrevision
    echo "otto 0.3.0" >> ${D}${sysconfdir}/hwrevision
    # sw-versions
    echo \
"firmware ${FIRMWARE_VERSION}\n\
software ${SOFTWARE_VERSION}\n\
mcu ${MCU_VERSION}" >> ${D}${sysconfdir}/sw-versions
}
