#!/bin/sh

### BEGIN INIT INFO
# Provides:          fbcp
# Default-Start:     3 5
# Default-Stop:      0 1 6
# Short-Description: fbcp init
# Description:       This file execute fbcp
### END INIT INFO

do_start() {
	/usr/bin/fbcp &
}

do_stop() {
	killall fbcp
}

case "$1" in
  start)
	do_start
	;;
  restart|reload|force-reload)
	echo "Error: argument '$1' not supported" >&2
	exit 3
	;;
  stop)
    do_stop
	;;
  *)
	echo "Usage: $0 start|stop" >&2
	exit 3
	;;
esac
