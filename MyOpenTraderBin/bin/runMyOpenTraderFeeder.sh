#!/bin/bash

java -server -cp "../libs/*" -Xms2g -Xmx2g -XX:MaxPermSize=512m -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.feeder.iab.MyOpenTraderFeederIAB ../conf $1 $2 $3 $4 $5 $6 $7 $8 $9


