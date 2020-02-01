DEPENDS_remove = "glib-2.0"
EXTRA_OECONF_append = "--disable-glib"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}_${PV}:"

SRC_URI_append = "file://0005-google-patches.patch"

FILES_${PN}_append = "${libdir}/*.so.*"
