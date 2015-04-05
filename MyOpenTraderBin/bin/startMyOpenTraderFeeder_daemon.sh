#!/bin/bash

cd $MOTHOME/bin

nohup ./runMyOpenTraderFeeder.sh -e OTHERS > ../logs/MOT-Feeder_OTHERS.log 2>&1 & echo $! > MOT-Feeder_OTHERS.pid  
sleep 5
nohup ./runMyOpenTraderFeeder.sh -e mot1 > ../logs/MOT-Feeder_mot1.log 2>&1 & echo $! > MOT-Feeder_mot1.pid  
sleep 5
nohup ./runMyOpenTraderFeeder.sh -e mot1 > ../logs/MOT-Feeder_mot2.log 2>&1 & echo $! > MOT-Feeder_mot2.pid  
sleep 5
nohup ./runMyOpenTraderFeeder.sh -e mot1 > ../logs/MOT-Feeder_mot3.log 2>&1 & echo $! > MOT-Feeder_mot3.pid  
