# Description
otto-bsp is intended to provide developers of the OTTO synth an easy to setup environment for OpenEmbedded/Yocto project development. This respository is set up with git submodules to link together all of the required BSP layers. In addition, a Vagrantfile is provided for those who do not already have Linux systems ready to quickly get started via a virtual machine.

# Getting started without an existing Linux system
If you do not already have a linux system available for development, you can use the supplied Vagrantfile. It will automatically set up an Ubuntu 18.10 based system with all the required dependencies and automatically download the source repository. You can download Vagrant from https://www.vagrantup.com/. If you would like to allocate more/less memory and CPU resources to the virtual machine, make sure to modify the `VM_MEMORY` and `VM_CORES` lines in the Vagrantfile prior to performing the `vagrant up` command.
At the moment, Virtualbox is supported. It must be installed prior to running this command.
```
vagrant up --provider=virtualbox
vagrant ssh
```

# Getting started with an existing Linux system
If you already have a linux system available for development, the dependencies for development are as follows:
```
gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat libsdl1.2-dev xterm repo
```

Once those are installed, you are ready to download the source:
```
git clone --recurse-submodules -b warrior https://github.com/OTTO-project/otto-bsp.git
```

Once the source is downloaded, you are ready to set up your local build directory and initiate a build:
```
MACHINE=otto-proto-v1 DISTRO=otto source setup-environment build
bitbake otto-image
```
Relative to the build folder, your compiled image will be located at
```
build/tmp/deploy/images/otto-proto-v1/otto-image-otto-proto-v1.rpi-sdimg
```
This image can be imaged to your SD card with the following command (where /dev/sdX is your sdcard), executed from the build folder:
```
sudo dd if=tmp/deploy/images/otto-proto-v1/otto-image-otto-proto-v1.rpi-sdimg of=/dev/sdX bs=1M && sync
```
# meta-otto

The otto-bsp repository exists to set up the build environment. The recipes which define the image build are located in the Yocto layer meta-otto, whose source may be found here: https://github.com/adorbs/meta-otto
