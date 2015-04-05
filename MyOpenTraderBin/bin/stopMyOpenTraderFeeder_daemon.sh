#!/bin/bash

cd $MOTHOME/bin

kill -9 -$(<"MOT-Feeder_OTHERS.pid")
kill -9 -$(<"MOT-Feeder_mot1.pid")
kill -9 -$(<"MOT-Feeder_mot2.pid")
kill -9 -$(<"MOT-Feeder_mot3.pid")

