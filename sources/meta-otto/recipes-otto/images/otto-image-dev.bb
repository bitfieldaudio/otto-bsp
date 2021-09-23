include otto-image.inc

DESCRIPTION = "OTTO Linux SD card image including development tools"

# Installs GDB to the image
IMAGE_FEATURES += "tools-debug"
# Install -dbg versions of packages
IMAGE_FEATURES += "dbg-pkgs"

# Add networking and SSH
IMAGE_INSTALL += "connman"
IMAGE_INSTALL += "connman-client"
IMAGE_INSTALL += "dropbear"
IMAGE_INSTALL += "gdb"
IMAGE_INSTALL += "otto-core-tests"
IMAGE_INSTALL += "swupdate-www"
IMAGE_INSTALL += "u-boot-fw-utils"

# Extra tools
IMAGE_INSTALL += "valgrind"

addtask do_add_init_scripts after do_rootfs before do_image
do_add_init_scripts() {
  install -d ${IMAGE_ROOTFS}/etc/rcS.d/
  cat > ${IMAGE_ROOTFS}/etc/rcS.d/Sconnman.sh <<EOF
#!/bin/sh

case "$1" in
  start)
    mkdir -p /var/run/dbus
    dbus-daemon --system
    connmand
	;;
  restart|reload|force-reload)
	echo "Error: argument '$1' not supported" >&2
	exit 3
	;;
  stop)
    killall connmand
	;;
  *)
	echo "Usage: $0 start|stop" >&2
	exit 3
	;;
esac
EOF
}
