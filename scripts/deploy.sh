#!/bin/bash
set -x

scp app/build/libs/houdini-bot-0.1.0-all.jar suppoze@172.104.134.26:houdini.jar
ssh suppoze@172.104.134.26 ./launch.sh