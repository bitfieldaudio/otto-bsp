# Run swupdate on swu files in dir
for swufile in $1/*.swu; do
  if [ -f "$swufile" ]; then
    echo "Found .SWU file! Running..."
    killall otto
    echo 
    . /usr/lib/swupdate/conf.d/09-swupdate-args
    /usr/bin/swupdate $SWUPDATE_ARGS -i "$swufile" 2>&1  > $TMPDIR/update.log
  fi
  return 0
done