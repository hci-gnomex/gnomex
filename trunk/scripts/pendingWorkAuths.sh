#!/bin/bash

ORION_HOME=../../
HIBERNATE_HOME=%ORION_HOME%/applications/hciEnv
LIB=$HIBERNATE_HOME/lib
GNOMEX_HOME=%ORION_HOME%/applications/gnomex
GNOMEX_JAR=$GNOMEX_HOME/gnomex.jar

JDBC_DRIVER=$ORION_HOME/lib/sqljdbc4.jar
CLASSPATH=".:$GNOMEX_JAR:$GNOMEX_HOME/lib:$JDBC_DRIVER:$HIBERNATE_HOME:$LIB/hibernate3.jar:$ORION_HOME/lib:$ORION_HOME/lib/hci_utils.jar:$ORION_HOME/lib/hci_framework.jar:$LIB/Hibernate3Utils.jar:$ORION_HOME/lib/dom4j-1.6.1.jar:$ORION_HOME/lib/log4j-1.2.11.jar:$ORION_HOME/lib/commons-logging-1.0.4.jar:$ORION_HOME/lib/commons-collections-2.1.1.jar:$ORION_HOME/lib/jta.jar:$ORION_HOME/lib/jdom.jar:%ORION_HOME%\mail.jar"
for JAR in $LIB/*.jar
do
CLASSPATH="$CLASSPATH:$JAR"
done
export CLASSPATH

java hci.gnomex.daemon.PendingWorkAuthd $*
