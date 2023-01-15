
DEPENDS += "u-boot-mkenvimage-native"
# Ensures the initial environment variables are written to a file that u-boot can read.
do_compile_append() {
  mkenvimage -s 0x4000 -o ${B}/${config}/uboot.env ${B}/${config}/u-boot-initial-env
}
# Deploy this so it can be found by wic.
do_deploy_append() {
  install -D -m 755 ${B}/${config}/uboot.env ${DEPLOYDIR}/uboot.env                    
}
