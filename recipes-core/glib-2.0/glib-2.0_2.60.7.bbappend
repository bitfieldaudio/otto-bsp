# Remove python3 from glib-2.0

do_install_append() {
  rm -f ${D}${bindir}/gtester-report
}

#CODEGEN_PYTHON_RDEPENDS = "python3 python3-distutils python3-xml"
CODEGEN_PYTHON_RDEPENDS = ""
