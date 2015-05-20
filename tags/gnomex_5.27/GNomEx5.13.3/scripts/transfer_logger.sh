#!/bin/bash
java -classpath /home/local/HCI/orionsrvs/fdtapplogger/gnomex_client.jar hci.gnomex.httpclient.TransferLoggerMain ${1+"$@"} -serverURL http://hci-bio-test.hci.utah.edu:8008
