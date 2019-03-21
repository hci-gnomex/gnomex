@echo off

set TOMCAT_HOME=..\..\..\
set COMMON_LIB=%TOMCAT_HOME%\lib

set GNOMEX_LIB=..\WEB-INF\lib
set GNOMEX_CLASSES=..\WEB-INF\classes

set CP=.;%GNOMEX_CLASSES%;

for %%i in (%GNOMEX_LIB%\*.jar) do call append_classpath.cmd %%i

for %%i in (%COMMON_LIB%\*.jar) do call append_classpath.cmd %%i

set PATH=%JAVA_HOME%\bin;%PATH%

java -classpath %CP% hci.gnomex.daemon.RemoveUserEmails %*
