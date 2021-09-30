# In do_postinst(), it should flash the mcu firmware
# Skip if version is the same as in /etc/sw-versions - the version is passed as the second argument.

do_postinst()
{
    echo "Checking if MCU should be updated..."
    VERSIONFILE="/etc/sw-versions"
    if [ ! -x "$VERSIONFILE" ]; then
        echo "$VERSIONFILE not found!"
        exit 1
    fi
    CURRENT=$(cat $VERSIONFILE | sed -n -e 's/^mcu //p')
    NEW=$1
    if [ ! $CURRENT==$NEW ]; then
        echo "Different MCU version. Flashing..."
        /home/root/otto-mcu-fw/load-otto-mcu-fw.sh
    else
        echo "MCU version unchanged. Skipping."
    fi
    exit 0
}

case "$1" in
preinst)
    exit 0
    ;;
postinst)
    do_postinst $2
    ;;
*)
    echo "default"
    exit 1
    ;;
esac
