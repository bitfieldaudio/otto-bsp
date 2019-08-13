#!/bin/sh
if [ -z "$SSH_CLIENT" ] ; then

amixer -c 0 set Headphone 100%
wait
/home/root/otto/otto-input&
cd /home/root/otto
/home/root/otto/otto

fi
