#!/bin/bash

cd $MOTHOME/bin 

nohup ./runSimulator.sh > ../logs/simulator.log 2>&1 & echo $! > simulator.pid  