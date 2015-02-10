#!/bin/bash

java -server -cp "../libs/*" -Xms2G -Xmx2G -XX:MaxPermSize=512m -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.core.MyOpenTraderCore -c ../conf $1 $2 $3 $4 $5 $6 $7 $8 $9


