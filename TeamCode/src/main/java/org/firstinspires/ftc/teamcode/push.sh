#!/bin/bash

if ! adb -v <adb> &> /dev/null
then
    echo "adb must be installed"
    exit
fi

adb push ${PWD} /mnt/sdcard/first/java/src/org/firstinspires/ftc/teamcode
