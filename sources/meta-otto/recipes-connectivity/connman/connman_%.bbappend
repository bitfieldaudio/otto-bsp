FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://Sconnman.sh"

do_install_append() {
  install -d ${D}${sysconfdir}/rcS.d
  install -m 755 ${WORKDIR}/Sconnman.sh ${D}${sysconfdir}/rcS.d/Sconnman.sh
}