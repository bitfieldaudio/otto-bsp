DEPENDS_remove = "glib-2.0"
EXTRA_OECONF_append = "--disable-glib"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}_${PV}:"

SRC_URI += "file://0001-Google-patches-OTTO-patches.patch"

FILES_${PN}_append = "${libdir}/*.so.*"

