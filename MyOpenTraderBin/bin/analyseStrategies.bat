@echo off

echo "*** Starting new StrategyAnalyser"
java -server -cp "../libs/*" -Xms1g -Xmx1g -XX:+AggressiveOpts -XX:+DisableExplicitGC org.mot.common.util.StrategyAnalyser ../conf %1
pause


