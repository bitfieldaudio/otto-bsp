# Deletes everything in /data/overrides after an update

do_postinst()
{
    rm -rf /data/overrides/*
    exit 0
}

case "$1" in
preinst)
    exit 0
    ;;
postinst)
    echo "Deleting overides"
    do_postinst
    ;;
*)
    echo "default"
    exit 1
    ;;
esac
