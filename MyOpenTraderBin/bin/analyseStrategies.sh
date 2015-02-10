#!/bin/bash

echo "*** Starting new StrategyAnalyser"
java -server -cp "../libs/*" -Xms1g -Xmx1g -XX:MaxPermSize=512m -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.common.util.StrategyAnalyser ../conf $1


