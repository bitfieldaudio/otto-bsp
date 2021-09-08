PACKAGECONFIG_CONFARGS = ""

SRC_URI += " \
    file://09-swupdate-args \
    file://swupdate.cfg \
    "

do_install_append() {
    install -m 0644 ${WORKDIR}/09-swupdate-args ${D}${libdir}/swupdate/conf.d/

    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/swupdate.cfg ${D}${sysconfdir}
}
