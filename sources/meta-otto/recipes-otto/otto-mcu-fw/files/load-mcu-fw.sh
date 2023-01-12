#!/bin/sh -x

cmd="program otto-mcu-fw.elf verify reset exit"
if [[ $1 == gdb ]]; then
  cmd="bindto 0.0.0.0; program otto-mcu-fw.elf verify reset"
fi

openocd -f $(dirname $0)/openocd.cfg -f target/<MCU_TYPE>.cfg -c "$cmd"
