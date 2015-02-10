#!/bin/bash

java -server -cp "../libs/*" -Xms3g -Xmx3g -XX:MaxPermSize=512m -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.core.simulation.SimulationRunner -c ../conf

