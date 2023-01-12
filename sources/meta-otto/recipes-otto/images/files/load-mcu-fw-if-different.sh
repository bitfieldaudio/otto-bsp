# Skip if version is the same as in /etc/sw-versions - the version is passed as the second argument.

echo "Checking if MCU should be updated..."
VERSIONFILE="/etc/sw-versions"
if [ ! -f "$VERSIONFILE" ]; then
    echo "$VERSIONFILE not found!"
    exit 1
fi
CURRENT=$(cat $VERSIONFILE | sed -n -e 's/^mcu //p')
NEW=$1
if [ ! $CURRENT==$NEW ]; then
    echo "Different MCU version. Flashing..."
    /home/root/otto-mcu-fw/load-mcu-fw.sh
else
    echo "MCU version unchanged. Skipping."
fi
