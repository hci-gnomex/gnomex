rem @echo off
set PATH=%JAVA_HOME%\bin;%PATH%
java -classpath .\gnomex.jar;..\..\lib\jdom.jar hci.gnomex.utility.CreateAnalysisMain %*