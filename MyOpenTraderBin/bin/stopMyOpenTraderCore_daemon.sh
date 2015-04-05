#!/bin/bash

cd $MOTHOME/bin

# Just kill the entire process...
kill -9 -$(<"MOT-Core_OTHERS.pid")
kill -9 -$(<"MOT-Core_mot1.pid")
kill -9 -$(<"MOT-Core_mot2.pid")
kill -9 -$(<"MOT-Core_mot3.pid")
kill -9 -$(<"scheduler.pid")
