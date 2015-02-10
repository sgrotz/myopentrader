#!/bin/bash

screen -A -m -d -L -S feederALL ./runMyOpenTraderFeeder.sh ALL
sleep 5
screen -A -m -d -L -S coreALL  ./runMyOpenTraderCore.sh ALL

