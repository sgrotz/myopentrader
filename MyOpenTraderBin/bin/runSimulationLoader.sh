#!/bin/bash

java -server -cp "../libs/*" -Xms3g -Xmx3g -XX:MaxPermSize=512m -XX:+DisableExplicitGC org.mot.core.simulation.SimulationLoader $1 $2 $3 $4 $5 $6


