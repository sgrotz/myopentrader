#!/bin/bash

cd $MOTHOME/bin

pkill -P `cat MOT-Feeder_OTHERS.pid`
pkill -P `cat MOT-Feeder_mot1.pid`
pkill -P `cat MOT-Feeder_mot2.pid`
pkill -P `cat MOT-Feeder_mot3.pid`

