#!/bin/sh
ip addr flush dev eth0
ip addr add 192.168.2.2/24 dev eth0
ip link set eth0 up

