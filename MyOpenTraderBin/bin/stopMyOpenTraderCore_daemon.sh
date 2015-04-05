#!/bin/bash

cd $MOTHOME/bin

# Just kill the entire process...
pkill -P `cat MOT-Core_OTHERS.pid`
pkill -P `cat MOT-Core_mot1.pid`
pkill -P `cat MOT-Core_mot2.pid`
pkill -P `cat MOT-Core_mot3.pid`
pkill -P `cat scheduler.pid`
