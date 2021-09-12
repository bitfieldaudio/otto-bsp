# In do_postinst(), it should flash the mcu firmware
# Skip if version is the same as in /etc/sw-versions - the version is passed as the second argument.

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
