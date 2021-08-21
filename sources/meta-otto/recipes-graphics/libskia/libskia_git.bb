LICENSE = "Unknown & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=822f02cc7736281816581cd064afbb1c \
                    file://include/third_party/skcms/LICENSE;md5=c37f94a6c8957870f13f5dd9b7e9f54f \
                    file://include/third_party/vulkan/LICENSE;md5=c37f94a6c8957870f13f5dd9b7e9f54f \
                    file://third_party/etc1/LICENSE;md5=906edf8ee5604a80fb397834958fc689 \
                    file://third_party/vulkanmemoryallocator/LICENSE;md5=d25bb58a1be2e1af9b58d31565a206dc \
                    file://third_party/vulkanmemoryallocator/include/LICENSE.txt;md5=762035fa7cfae632cd5297e05aac6b82 \
                    file://third_party/wuffs/LICENSE;md5=c37f94a6c8957870f13f5dd9b7e9f54f \
                    file://third_party/skcms/LICENSE;md5=c37f94a6c8957870f13f5dd9b7e9f54f \
                    file://modules/canvaskit/canvaskit/LICENSE;md5=d25bb58a1be2e1af9b58d31565a206dc \
                    file://modules/pathkit/npm-asmjs/LICENSE;md5=d25bb58a1be2e1af9b58d31565a206dc \
                    file://modules/pathkit/npm-wasm/LICENSE;md5=d25bb58a1be2e1af9b58d31565a206dc"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "1ae440a3cb3c48d769d8d5fe7c95fbba818feed2"

SRC_URI = "git://skia.googlesource.com/skia.git;protocol=https"
S = "${WORKDIR}/git"
B = "${S}/out/Release"

require gn-utils.inc

DEPENDS += " \
  ninja-native \
  virtual/libgles2 userland \
  libpng \
"
#RDEPENDS_${PN}-staticdev += "libpng"

GN_ARGS = " \
is_component_build=true \
is_debug=false \
is_official_build=true \
is_clang=false \
"

# Toolchains we will use for the build. We need to point to the toolchain file
# we've created, set the right target architecture and make sure we are not
# using Chromium's toolchain (bundled clang, bundled binutils etc).
GN_ARGS += ' \
        use_gold=${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', 'true', 'false', d)} \
'


GN_ARGS += "\
paragraph_gms_enabled=false \
paragraph_tests_enabled=false \
skia_enable_android_utils=false \
skia_enable_api_available_macro=true \
skia_enable_ccpr=true \
skia_enable_direct3d_debug_layer=false \
skia_enable_discrete_gpu=true \
skia_enable_fontmgr_FontConfigInterface=false \
skia_enable_fontmgr_android=false \
skia_enable_fontmgr_custom_directory=true \
skia_enable_fontmgr_custom_embedded=true \
skia_enable_fontmgr_custom_empty=true \
skia_enable_fontmgr_fontconfig=false \
skia_enable_gpu=true \
skia_enable_gpu_debug_layers=true \
skia_enable_nvpr=false \
skia_enable_particles=false \
skia_enable_pdf=false \
skia_enable_skottie=false \
skia_enable_skparagraph=true \
skia_enable_skrive=false \
skia_enable_skshaper=false \
skia_enable_sksl_interpreter=false \
skia_enable_skvm_jit=false \
skia_enable_spirv_validation=false \
skia_enable_tools=false \
skia_enable_vulkan_debug_layers=false \
skia_use_dng_sdk=false \
skia_use_expat=false \
skia_use_fontconfig=false \
skia_use_freetype=true \
skia_use_gl=true \
skia_use_harfbuzz=false \
skia_use_icu=false \
skia_use_libgifcodec=false \
skia_use_libheif=false \
skia_use_libjpeg_turbo_decode=false \
skia_use_libjpeg_turbo_encode=false \
skia_use_libpng_decode=false \
skia_use_libpng_encode=false \
skia_use_libwebp_decode=false \
skia_use_libwebp_encode=false \
skia_use_lua=false \
skia_use_piex=false \
skia_use_sfntly=false \
skia_use_system_freetype2=false \
skia_use_x11=false \
skia_use_xps=false \
skia_use_zlib=false \
skia_use_egl=true \
"

EXTRA_BIN="${WORKDIR}/extra-bin"

do_fetch_third_party() {
  cd ${S}

  # An alias is not enough, it makes is_clang.py fail.
  mkdir -p ${EXTRA_BIN}
  [ -e ${EXTRA_BIN}/python ] && rm ${EXTRA_BIN}/python
  ln -s $(which python2) ${EXTRA_BIN}/python
  export PATH="${EXTRA_BIN}:$PATH"

  python tools/git-sync-deps
}
addtask fetch_third_party after do_patch before do_configure

do_configure() {
  cd ${S}
  export PATH="${EXTRA_BIN}:$PATH"
  ./bin/gn gen '--args=${GN_ARGS} ${@get_toolchain_args(d)}' "${B}"
}

do_compile() {
  export PATH="${EXTRA_BIN}:$PATH"
  ninja -v ${PARALLEL_MAKE} -C "${B}"
}
do_compile[progress] = "outof:^\[(\d+)/(\d+)\]\s+"

# Because libskia.so is unversioned (i.e. not libskia.so.1)
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

do_install() {
  install -d ${D}/${libdir}/
  install -m 0755 ${B}/libskia.so ${D}/${libdir}/

  install -d ${D}/${includedir}/skia/
  # Copy over header files for including by other packages
	cp -r ${S}/include ${D}${includedir}/skia/include
  find ${D}/${includedir}/skia/include/ -type f -exec chmod 0755 {} \;

  # Get defines with `bin/gn desc out/Release/ //:skia defines`
  # TODO: Consider automating importing these, but currently
  # SKIA_IMPLEMENTATION=1 is filtered out manually
  install -d ${D}${datadir}/pkgconfig
  echo "
Name: skia
Version: 1
Description: An open source 2D graphics library
Cflags: \
  -I${includedir}/skia \
  -I${includedir}/skia/include/c \
  -I${includedir}/skia/include/core \
  -I${includedir}/skia/include/gpu \
  -I${includedir}/skia/include/effects \
  -DSK_HAS_ANDROID_CODEC \
  -DSK_R32_SHIFT=16 \
  -DSK_GAMMA_APPLY_TO_A8 \
  -DSK_ALLOW_STATIC_GLOBAL_INITIALIZERS=1 \
  -DGR_TEST_UTILS=1 \
  -DSK_GL \
  -DSK_ENABLE_DUMP_GPU \
  -DSKVM_JIT_WHEN_POSSIBLE
Libs: -lskia -lpng" > ${D}${datadir}/pkgconfig/skia.pc
}

RPROVIDES = ""

FILES_${PN}-dev += "${includedir}/skia"
FILES_${PN}-dev += "${datadir}/pkgconfig"

