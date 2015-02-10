@echo off

call cleanRelease.bat

call mvn clean compile package install javadoc:javadoc -DskipTests

ant build

pause





