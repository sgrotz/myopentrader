@echo off

echo "*** Starting new embedded MyOpenTrader Web Frontend"
java -server -cp "../libs/*" -Xms3g -Xmx3g -XX:MaxPermSize=512m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.common.server.EmbeddedWebServer -c ../conf %1 %2 %3 %4 %5


