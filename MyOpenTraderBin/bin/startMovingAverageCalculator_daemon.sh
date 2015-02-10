#!/bin/bash

screen -A -m -d -S motSimulator java -server -Xms5g -Xmx5g -XX:MaxPermSize=512m -XX:+DisableExplicitGC -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=6901 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar MovingAverageSimulator.jar conf >> logs/simulation.log


