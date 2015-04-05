#!/bin/bash

cd $MOTHOME/bin

nohup ./runMyOpenTraderCore.sh -e OTHERS > ../logs/MOT-Core_OTHERS.log 2>&1 & echo $! > MOT-Core_OTHERS.pid  
sleep 5
nohup ./runMyOpenTraderCore.sh -e mot1 > ../logs/MOT-Core_mot1.log 2>&1 & echo $! > MOT-Core_mot1.pid  
sleep 5
nohup ./runMyOpenTraderCore.sh -e mot2 > ../logs/MOT-Core_mot2 2>&1 & echo $! > MOT-Core_mot2.pid  
sleep 5
nohup ./runMyOpenTraderCore.sh -e mot3 > ../logs/MOT-Core_mot3 2>&1 & echo $! > MOT-Core_mot3.pid  
sleep 5
nohup ./runMyOpenTraderCore.sh -e scheduler > ../logs/scheduler.log 2>&1 & echo $! > scheduler.pid  

