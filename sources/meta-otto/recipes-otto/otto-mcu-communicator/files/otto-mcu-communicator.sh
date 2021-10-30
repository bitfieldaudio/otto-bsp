#!/bin/sh

do_start() {
  /home/root/otto-mcu-communicator/mcu_service &
}

do_stop() {
	killall mcu-service
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
