@echo off

set TOMCAT_HOME=..\..\..\
set COMMON_LIB=%TOMCAT_HOME%\common\lib
set OPENEJB=%TOMCAT_HOME%\webapps\openejb\lib

set GNOMEX_LIB=..\WEB-INF\lib
set GNOMEX_CLASSES=..\WEB-INF\classes

set CP=.;%GNOMEX_CLASSES%;%OPENEJB%\commons-logging-1.1.jar;%OPENEJB%\commons-collections-3.2.jar;%OPENEJB%\javaee-api-5.0-3.jar;

for %%i in (%GNOMEX_LIB%\*.jar) do call append_classpath.cmd %%i

for %%i in (%COMMON_LIB%\*.jar) do call append_classpath.cmd %%i

set PATH=%JAVA_HOME%\bin;%PATH%

java -classpath %CP% hci.gnomex.daemon.RegisterFiles %*