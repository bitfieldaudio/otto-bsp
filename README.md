# Description
otto-bsp is intended to provide developers of the OTTO synth an easy to setup environment for OpenEmbedded/Yocto project development. This respository is set up with git submodules to link together all of the required BSP layers.

# Getting started with an existing Linux system
If you already have a linux system available for development, the dependencies for development are as follows:
```
gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat libsdl1.2-dev xterm
```

Once those are installed, you are ready to download the source:
```
git clone --recurse-submodules https://github.com/OTTO-project/otto-bsp.git
```

Once the source is downloaded, you are ready to set up your local build directory and initiate a build:
```
MACHINE=otto-beta-v0.1.0 DISTRO=otto source setup-environment build
bitbake otto-image-dev
```
The `MACHINE` environment variable corresponds roughly to iterations of the hardware - changing things such as pin layout and peripherals. Currently, this is the most recent version.  Building the `-dev` image includes an SSH server ond some development tools. If this is not needed, you can `bitbake otto-image`.
Relative to the build folder, your compiled image will be located at
```
build/tmp/deploy/images/otto-beta-v0.1.0/otto-image-dev-otto-beta-v0.1.0.wic
```
This image can be flashed to your SD card with the following command (where /dev/sdX is your sdcard), executed from the build folder:
```
sudo dd if=tmp/deploy/images/otto-beta-v0.1.0/otto-image-dev-otto-beta-v0.1.0.wic of=/dev/sdX bs=1M && sync
```
The `wic` tool also works with [bmap](https://github.com/intel/bmap-tools) which should be faster than `dd`. You are welcome to try this out.

# Getting started without an existing Linux system
If you do not already have a linux system available for development, the Yocto project has been tested to work on Ubuntu 20.04 on Windows Subsystem for Linux 2 (WSL2), which allow you to proceed as above.

Some dependencies that were assumed to be included above, might not be included from this approach. This should become apparent through the process. 

You might not find your SD card in /dev/sdX, but you can use a program like BalenaEtcher to flash your SD-card. Note that you will need to flash the most recent build:  `otto-image-otto-beta-v0.1.0-SOMEDATEANDVERSION.rootfs.wic` and you cannot just flash `otto-image-dev-otto-beta-v0.1.0.wic` since this is a symlink and Windows does not always resolve these.
