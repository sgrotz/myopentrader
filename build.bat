@echo off

call mvn clean compile package install javadoc:javadoc -DskipTests

