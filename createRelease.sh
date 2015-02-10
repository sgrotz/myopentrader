#!/bin/bash

mvn clean compile package install javadoc:javadoc -DskipTests

ant build



