#!/bin/bash

screen -A -m -d -L -S feederALL ./runMyOpenTraderFeeder.sh OTHERS
sleep 5
screen -A -m -d -L -S feederMot1 ./runMyOpenTraderFeeder.sh mot1 
sleep 5
screen -A -m -d -L -S feederMot2 ./runMyOpenTraderFeeder.sh mot2
sleep 5
screen -A -m -d -L -S feederMot3 ./runMyOpenTraderFeeder.sh mot3
sleep 5
#DISABLED TO FREE RESOURCES
# screen -A -m -d -L -S feederFX ./runMyOpenTraderFeeder.sh FX 
