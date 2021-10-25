# Run swupdate on swu files in dir
for swufile in $1/*.swu; do
  if [ -f "$swufile" ]; then
    killall otto
    echo "Found .SWU file! Running..." > /dev/tty1
    . /usr/lib/swupdate/conf.d/09-swupdate-args
    /usr/bin/swupdate $SWUPDATE_ARGS -i "$swufile" 2>&1  | tee /dev/tty1 $TMPDIR/update.log
  fi
  return 0
done