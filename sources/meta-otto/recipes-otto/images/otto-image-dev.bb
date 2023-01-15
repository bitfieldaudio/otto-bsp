require otto-image.inc

DESCRIPTION = "OTTO Linux SD card image including development tools"

# Installs GDB, strace to the image
IMAGE_FEATURES += "tools-debug"
# Install -dbg versions of packages
# NOTE, for some reason u-boot cannot load the kernel with this set!
#IMAGE_FEATURES += "dbg-pkgs"

# Add networking and SSH
IMAGE_INSTALL += "connman"
IMAGE_INSTALL += "connman-client"
IMAGE_INSTALL += "dropbear"

# Web-client to swupdate. On port 5011
IMAGE_INSTALL += "swupdate-www"
# Tests for otto-core
IMAGE_INSTALL += "otto-core-tests"

# Better tools to get/set u-boot variables
IMAGE_INSTALL += "u-boot-fw-utils"

# Extra tools
IMAGE_INSTALL += "valgrind"
