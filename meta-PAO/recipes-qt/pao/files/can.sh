#!/bin/sh
ip link set can0 down
ip link set can0 up type can bitrate 125000
