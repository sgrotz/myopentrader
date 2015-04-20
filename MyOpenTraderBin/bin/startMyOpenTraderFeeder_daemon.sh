#!/bin/bash

cd $MOTHOME/bin

echo "*** Starting MyOpenTraderFeeder - Others ***"
echo "*** Starting MyOpenTraderFeeder - Others ***" >> ../logs/MOT-Feeder_OTHERS.log
date >> ../logs/MOT-Feeder_OTHERS.log
nohup ./runMyOpenTraderFeeder.sh OTHERS >> ../logs/MOT-Feeder_OTHERS.log 2>&1 & echo $! > MOT-Feeder_OTHERS.pid  
sleep 5

echo "*** Starting MyOpenTraderFeeder - mot1 ***"
echo "*** Starting MyOpenTraderFeeder - mot1 ***" >> ../logs/MOT-Feeder_mot1.log
date >> ../logs/MOT-Feeder_mot1.log
nohup ./runMyOpenTraderFeeder.sh mot1 >> ../logs/MOT-Feeder_mot1.log 2>&1 & echo $! > MOT-Feeder_mot1.pid  
sleep 5

echo "*** Starting MyOpenTraderFeeder - mot2 ***"
echo "*** Starting MyOpenTraderFeeder - mot2 ***" >> ../logs/MOT-Feeder_mot2.log
date >> ../logs/MOT-Feeder_mot2.log
nohup ./runMyOpenTraderFeeder.sh mot2 >> ../logs/MOT-Feeder_mot2.log 2>&1 & echo $! > MOT-Feeder_mot2.pid  
sleep 5

echo "*** Starting MyOpenTraderFeeder - mot3 ***"
echo "*** Starting MyOpenTraderFeeder - mot3 ***" >> ../logs/MOT-Feeder_mot3.log
date >> ../logs/MOT-Feeder_mot3.log
nohup ./runMyOpenTraderFeeder.sh mot3 >> ../logs/MOT-Feeder_mot3.log 2>&1 & echo $! > MOT-Feeder_mot3.pid  
