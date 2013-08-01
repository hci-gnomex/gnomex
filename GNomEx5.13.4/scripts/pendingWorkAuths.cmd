@echo off

set ORION_HOME=..\..\
set HIBERNATE_HOME=%ORION_HOME%\applications\hciEnv
set LIB=%HIBERNATE_HOME%\lib
set GNOMEX_HOME=%ORION_HOME%\applications\gnomex
set GNOMEX_JAR=%GNOMEX_HOME%\gnomex.jar

set JDBC_DRIVER=%ORION_HOME%\lib\sqljdbc4.jar
set CP=.;%GNOMEX_JAR%;%GNOMEX_HOME%\lib;%JDBC_DRIVER%;%HIBERNATE_HOME%;%LIB%\hibernate3.jar;%ORION_HOME%\lib;%ORION_HOME%\lib\hci_utils.jar;%ORION_HOME%\lib\hci_framework.jar;%LIB%\Hibernate3Utils.jar;%ORION_HOME%\lib\dom4j-1.6.1.jar;%ORION_HOME%\lib\log4j-1.2.11.jar;%ORION_HOME%\lib\commons-logging-1.0.4.jar;%ORION_HOME%\lib\commons-collections-2.1.1.jar;%ORION_HOME%\lib\jta.jar;%ORION_HOME%\lib\jdom.jar;%ORION_HOME%\mail.jar;
for %%i in (%LIB%\*.jar) do call append_classpath.cmd %%i

set PATH=%JAVA_HOME%\bin;%PATH%

java -classpath %CP% hci.gnomex.daemon.PendingWorkAuthd %*