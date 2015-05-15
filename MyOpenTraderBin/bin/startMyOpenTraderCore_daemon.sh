#!/bin/bash

cd $MOTHOME/bin

echo "*** Starting MyOpenTraderCore - OTHERS ***"
echo "*** Starting MyOpenTraderCore - OTHERS ***" >> ../logs/MOT-Core_OTHERS.log
date >> ../logs/MOT-Core_OTHERS.log
nohup ./runMyOpenTraderCore.sh -e OTHERS >> ../logs/MOT-Core_OTHERS.log 2>&1 & echo $! > MOT-Core_OTHERS.pid  
sleep 5

echo "*** Starting MyOpenTraderCore - mot1 ***"
echo "*** Starting MyOpenTraderCore - mot1 ***" >> ../logs/MOT-Core_mot1.log
date >> ../logs/MOT-Core_mot1.log
nohup ./runMyOpenTraderCore.sh -e mot1 >> ../logs/MOT-Core_mot1.log 2>&1 & echo $! > MOT-Core_mot1.pid  
sleep 5

echo "*** Starting MyOpenTraderCore - mot2 ***"
echo "*** Starting MyOpenTraderCore - mot2 ***" >> ../logs/MOT-Core_mot2.log
date >> ../logs/MOT-Core_mot2.log
nohup ./runMyOpenTraderCore.sh -e mot2 >> ../logs/MOT-Core_mot2.log 2>&1 & echo $! > MOT-Core_mot2.pid  
sleep 5

echo "*** Starting MyOpenTraderCore - mot3 ***"
echo "*** Starting MyOpenTraderCore - mot3 ***" >> ../logs/MOT-Core_mot3.log
date >> ../logs/MOT-Core_mot3.log
nohup ./runMyOpenTraderCore.sh -e mot3 >> ../logs/MOT-Core_mot3.log 2>&1 & echo $! > MOT-Core_mot3.pid  
sleep 5

echo "*** Starting MyOpenTraderCore - scheduler ***"
echo "*** Starting MyOpenTraderCore - scheduler ***" >> ../logs/scheduler.log
date >> ../logs/scheduler.log
nohup ./runMyOpenTraderCore.sh -e scheduler >> ../logs/scheduler.log 2>&1 & echo $! > scheduler.pid  

