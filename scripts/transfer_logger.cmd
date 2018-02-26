@echo off
java -classpath ..\WEB-INF\lib\gnomex_client.jar hci.gnomex.httpclient.TransferLoggerMain %* -server hci-bio-test
