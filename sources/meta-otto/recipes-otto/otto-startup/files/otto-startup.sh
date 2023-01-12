#!/bin/sh
if [ -z "$SSH_CLIENT" ] ; then

cd /home/root/otto-mcu-fw
./load-mcu-fw.sh
cd /home/root/otto
/home/root/otto/otto

fi
