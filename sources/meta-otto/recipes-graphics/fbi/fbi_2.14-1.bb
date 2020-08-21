HOMEPAGE = "https://www.kraxel.org/blog/linux/fbida/"
DESCRIPTION = "frame buffer image viewer"

SRC_URI = "https://git.kraxel.org/cgit/fbida/snapshot/fbida-${PV}.tar.gz"
SRC_URI[md5sum] = "bda55b3deaf8bbe497a60be7b1851bf0"
SRC_URI[sha256sum] = "b6ec7bda0a6d29d2848ee2a7a67a3ba1af0bc6493d69c149d95b609d60b469a3"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e8feb78a32950a909621bbb51f634b39"

S = "${WORKDIR}/fbida-${PV}"

CFLAGS_append = " ${LDFLAGS}"" "

EXTRA_OEMAKE = "'CC=${CC}' 'RANLIB=${RANLIB}' 'AR=${AR}' 'CFLAGS=${CFLAGS} 'BUILDDIR=${S}'"
