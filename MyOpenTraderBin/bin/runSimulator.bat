@echo off

java -server -cp "../libs/*" -Xms3g -Xmx3g -XX:MaxPermSize=512m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.core.simulation.SimulationRunner -c ../conf

pause