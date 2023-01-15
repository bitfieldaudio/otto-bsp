#!/bin/sh

case "$1" in
  start)
    mkdir -p /var/run/dbus
    dbus-daemon --system
    connmand
	;;
  restart|reload|force-reload)
	echo "Error: argument '$1' not supported" >&2
	exit 3
	;;
  stop)
    killall connmand
	;;
  *)
	echo "Usage: $0 start|stop" >&2
	exit 3
	;;
esac