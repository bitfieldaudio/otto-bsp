DESCRIPTION ?= "TrueType font package ${PN}"
SECTION = "fonts"
SUMMARY = "Google Roboto Fonts"
HOMEPAGE = "https://github.com/google/roboto"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "https://github.com/google/roboto/releases/download/v2.138/roboto-android.zip"
SRC_URI[md5sum] = "3b43a5cb33196ec25e44d5fcb40219e1"

S = "${WORKDIR}"

# we don't need a compiler nor a c library for these fonts
INHIBIT_DEFAULT_DEPS = "1"

FILES_${PN} = "${datadir}/fonts/truetype/RobotoCondensed-MediumItalic.ttf \
	${datadir}/fonts/truetype/Roboto-Light.ttf \
	${datadir}/fonts/truetype/Roboto-BoldItalic.ttf \
	${datadir}/fonts/truetype/Roboto-Thin.ttf \
	${datadir}/fonts/truetype/Roboto-Regular.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-BoldItalic.ttf \
	${datadir}/fonts/truetype/Roboto-Italic.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-Bold.ttf \
	${datadir}/fonts/truetype/Roboto-BlackItalic.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-Medium.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-LightItalic.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-Regular.ttf \
	${datadir}/fonts/truetype/Roboto-Black.ttf \
	${datadir}/fonts/truetype/Roboto-Medium.ttf \
	${datadir}/fonts/truetype/Roboto-Bold.ttf \
	${datadir}/fonts/truetype/Roboto-ThinItalic.ttf \
	${datadir}/fonts/truetype/Roboto-LightItalic.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-Light.ttf \
	${datadir}/fonts/truetype/RobotoCondensed-Italic.ttf \
	${datadir}/fonts/truetype/Roboto-MediumItalic.ttf \
"

do_install() {
    install -d ${D}${datadir}/fonts/truetype/
    find ./ -name '*.tt[cf]' -exec install -m 0644 {} ${D}${datadir}/fonts/truetype/ \;
}

inherit allarch fontcache
