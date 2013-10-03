#!/bin/bash
java -classpath ../lib/gnomex_client.jar hci.gnomex.httpclient.TransferLoggerMain ${1+"$@"} -serverURL http://hci-bio-test.hci.utah.edu:8008
