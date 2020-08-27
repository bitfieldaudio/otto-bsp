#!/bin/sh

do_start() {
	/usr/bin/fbcp-ili9341 &
}

do_stop() {
	killall fbcp-ili9341
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
