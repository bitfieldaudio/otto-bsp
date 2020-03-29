#!/bin/sh -x

openocd -f openocd.cfg -f target/stm32f1x.cfg -c "program toot-mcu-fw.elf verify reset exit"