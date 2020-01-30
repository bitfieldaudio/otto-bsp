#!/bin/sh
if [ -z "$SSH_CLIENT" ] ; then

cd /home/root/toot-mcu-fw
./load-toot-mcu-fw.sh
/home/root/otto/otto-input&
cd /home/root/otto
/home/root/otto/otto

fi
