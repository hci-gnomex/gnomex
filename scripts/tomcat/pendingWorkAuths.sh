#!/bin/bash

TOMCAT_HOME=../../../
COMMON_LIB=$TOMCAT_HOME/common/lib
OPENEJB_LIB=$TOMCAT_HOME/webapps/openejb/lib

GNOMEX_LIB=../WEB-INF/lib
GNOMEX_CLASSES=../WEB-INF/classes

CLASSPATH=".:$GNOMEX_CLASSES:$OPENEJB_LIB\commons-logging-1.1.jar:$OPENEJB_LIB\commons-collections-3.2.jar:$OPENEJB_LIB\javaee-api-5.0-3.jar:"

for JAR in $COMMON_LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"

for JAR in $GNOMEX_LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"

done
export CLASSPATH

java hci.gnomex.daemon.PendingWorkAuthd $*
