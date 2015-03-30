@echo off

call cleanRelease.bat

REM Building the artefacts
call mvn clean compile package install javadoc:javadoc -DskipTests

REM Calling the ant script to package and distribute
ant build

pause





