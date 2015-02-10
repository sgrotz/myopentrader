@echo off

java -server -cp "../libs/*" -Xms3g -Xmx3g -XX:MaxPermSize=512m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.core.MyOpenTraderCore -c ../conf %1 %2 %3 %4 %5 %6 %7 %8 %9


