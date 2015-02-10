#!/bin/bash

screen -A -m -d -S coreALL ./runMyOpenTraderCore.sh -e OTHERS
sleep 5
screen -A -m -d -S coreMot1 ./runMyOpenTraderCore.sh -e mot1
sleep 5
screen -A -m -d -S coreMot2 ./runMyOpenTraderCore.sh -e mot2
sleep 5
screen -A -m -d -S coreMot3 ./runMyOpenTraderCore.sh -e mot3
sleep 5
# DISABLED 
# screen -A -m -d -S coreFX ./runMyOpenTraderCore.sh FX >> logs/coreFX.log

