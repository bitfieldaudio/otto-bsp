include otto-image.inc

DESCRIPTION = "OTTO Linux SD card image including development tools"

# Installs GDB to the image
IMAGE_FEATURES += "tools-debug"

# Add networking and SSH
IMAGE_INSTALL += "connman"
IMAGE_INSTALL += "connman-client"
IMAGE_INSTALL += "dropbear"

# Extra tools
IMAGE_INSTALL += "valgrind"
