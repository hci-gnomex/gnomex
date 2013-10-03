#!/bin/bash
CLASSPATH="$CLASSPATH:../lib/gnomex_client.jar"
export CLASSPATH
java hci.gnomex.httpclient.CreateAnalysisMain ${1+"$@"}
