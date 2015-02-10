#!/bin/bash

echo "*** Starting new embedded Message Bus"
java -server -cp "../libs/*" -Xms3g -Xmx3g -XX:MaxPermSize=512m -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.common.mq.EmbeddedBroker -c ../conf $1


