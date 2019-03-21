#!/bin/bash

TOMCAT_HOME=../../../
COMMON_LIB=$TOMCAT_HOME/lib

GNOMEX_LIB=../WEB-INF/lib
GNOMEX_CLASSES=../WEB-INF/classes

CLASSPATH=".:$GNOMEX_CLASSES:"

for JAR in $COMMON_LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"
done

for JAR in $GNOMEX_LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"

done
export CLASSPATH

CLASSPATH="./gnomex.jar:./gnomex_client.jar:$CLASSPATH"
export CLASSPATH

java -Xmx4000M hci.gnomex.daemon.LinkFastqData $*
