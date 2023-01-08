# Build docker image
docker build --build-arg "HOST_UID=$(id -u)" --build-arg "HOST_GID=$(id -g)" -t otto-docker .
# Create dir for downloads and sstate, if it does not exist
mkdir -p bitbake-data
# Run docker image
# Mount current directory (source directory) as bind mount
# Mount SSTATE and DL directories as a volume
docker run -it --mount type=bind,src="$(pwd)",target=/otto-bsp --mount type=bind,src="$(pwd)/bitbake-data",target=/bitbake-vol otto-docker bash