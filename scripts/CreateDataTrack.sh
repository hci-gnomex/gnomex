#!/bin/bash

TOMCAT_HOME=../../..
COMMON_LIB=$TOMCAT_HOME/lib
GNOMEX_LIB=../WEB-INF/lib
GNOMEX_CLASSES=../WEB-INF/classes

CLASSPATH=".:$GNOMEX_CLASSES"

for JAR in $COMMON_LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"
done

for JAR in $GNOMEX_LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"
done

export CLASSPATH
echo $CLASSPATH

PATH="$PATH:$GNOMEX_CLASSES:"
export PATH
echo $PATH

java -Xmx2G hci.gnomex.daemon.CreateDataTracks $*
