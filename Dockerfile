# The only thing that holds us back on Ubuntu 18.04 is Vivado 2018.3 (used to build green-mango-board).
# Everything else works in Ubuntu 20.04 already.
FROM ubuntu:20.04

# Do all of the following in a single command to avoid that Docker caches
# the "update" separately from the "install".
RUN apt-get update && \
    # Install software-properties-common to get add-apt-repository
    apt-get install -y software-properties-common && \
    # Add ppa:deadsnakes for python3.10
    add-apt-repository -y ppa:deadsnakes/ppa && \
    apt-get update && \
    apt-get install -y \
    # Yocto (hardknott) system requirements.
    # Reference: https://docs.yoctoproject.org/3.3.4/ref-manual/system-requirements.html#ubuntu-and-debian
    gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio \
    python3.10 python3.10-distutils python3.10-venv python3-pexpect \
    python3-git pylint3 python3-jinja2 xz-utils debianutils iputils-ping xterm zstd \
    liblz4-tool libegl1-mesa libsdl1.2-dev xterm python3-subunit mesa-common-dev \
    # We also need python2 for now
    python2.7 \
    # For `locale-gen` that we use later in this script
    locales

RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.10 1
RUN update-alternatives --set python3 /usr/bin/python3.10

RUN update-alternatives --install /usr/bin/python2 python2 /usr/bin/python2.7 1
RUN update-alternatives --set python2 /usr/bin/python2.7

# Install pip
RUN python3 -m ensurepip --default-pip --user

# Yocto requires a UTF8-based locale.
RUN locale-gen en_US.UTF-8 && update-locale LC_ALL=en_US.UTF-8 \
    LANG=en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

# Make user
#
# We will bind mount the project directory to the container.
# The SSTATE directory and the DL directory will both be in docker volumes,
# But the build directory containing the built artifacts will created in
# the project directory by docker. For this, it needs to use a user with the same
# User ID as the project directory owner (the user on the local machine).
#
# The following creates a user (yoctouser),with default UID of 1000.
# Overwrite this when building the docker image with the options 
# ```
# --build-arg "HOST_UID=$(id -u)" --build-arg "HOST_GID=$(id -g)"
# ```
ARG USER_NAME=yoctouser
ARG HOST_UID=1000
ARG HOST_GID=1000

RUN groupadd --gid ${HOST_UID} ${USER_NAME}
RUN adduser --disabled-password --gecos '' \
    --uid ${HOST_UID} \
    --gid ${HOST_GID} \
    ${USER_NAME}

USER ${USER_NAME}
ENV HOME /home/${USER_NAME}

# Set working directory to the project directory. This should be mounted on run.
WORKDIR /otto-bsp
# Current values for convenience. Increment these if needed.
ENV MACHINE=otto-beta-v0.3.0
ENV DISTRO=otto
# Default command is to prepare the environment for building the yocto project
CMD bash

