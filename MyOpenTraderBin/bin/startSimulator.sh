#!/bin/bash

cd $MOTHOME/bin 

echo "*** Starting MyOpenTrader Simulator ***"
echo "*** Starting MyOpenTrader Simulator ***" >> ../logs/simulator.log 
date >> ../logs/simulator.log 
nohup ./runSimulator.sh >> ../logs/simulator.log 2>&1 & echo $! > simulator.pid  

