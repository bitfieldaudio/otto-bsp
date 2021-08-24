# In do_postinst(), it should flash the mcu firmware
# (optionally, skip if version is the same as in /etc/sw-versions), 
# then set /etc/sw-versions to the appropriate version number

do_preinst()
{
    echo "do_preinst"
    exit 0
}

do_postinst()
{
    echo "do_postinst"
    exit 0
}

case "$1" in
preinst)
    echo "call do_preinst"
    do_preinst
    ;;
postinst)
    echo "call do_postinst"
    do_postinst
    ;;
*)
    echo "default"
    exit 1
    ;;
esac