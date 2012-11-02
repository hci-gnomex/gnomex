#!/bin/bash
java -classpath /home/local/HCI/orionsrvs/fdtapplogger/gnomex_client.jar hci.gnomex.httpclient.TransferLoggerMain "$@" -server hci-bio-test
