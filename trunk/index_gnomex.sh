#!/bin/bash


ORION_HOME=/home/tomcatadmin/orion
HIBERNATE_HOME=/home/tomcatadmin/orion/applications/hciEnv
LIB=$HIBERNATE_HOME/lib
GNOMEX_HOME=/home/tomcatadmin/orion/applications/gnomex
GNOMEX_JAR=$GNOMEX_HOME/gnomex.jar
LUCENE_JAR=$GNOMEX_HOME/lib/lucene-core-2.2.0.jar
JDBC_DRIVER=$ORION_HOME/lib/mysql-connector-java-5.1.7-bin.jar

CLASSPATH=".:$GNOMEX_JAR:$GNOMEX_HOME/lib:$LUCENE_JAR:$JDBC_DRIVER:$HIBERNATE_HOME:$LIB/hibernate3.jar:$ORION_HOME/lib:$ORION_HOME/lib/hci_utils.jar:$ORION_HOME/lib/hci_framework.jar:$LIB/Hibernate3Utils.jar:$ORION_HOME/lib/dom4j-1.6.1.jar:$ORION_HOME/lib/log4j-1.2.11.jar:$ORION_HOME/lib/commons-logging-1.0.4.jar:$ORION_HOME/lib/commons-collections-2.1.1.jar:$ORION_HOME/lib/jta.jar:$ORION_HOME/lib/jdom.jar"
for JAR in $LIB/*.jar
do
   CLASSPATH="$CLASSPATH:$JAR"
done
export CLASSPATH


java hci.gnomex.lucene.BuildSearchIndex 