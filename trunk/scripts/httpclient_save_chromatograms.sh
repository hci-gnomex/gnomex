#!/bin/bash
CLASSPATH="$CLASSPATH:./gnomex_client.jar"
export CLASSPATH
java hci.gnomex.httpclient.SaveChromatogramsFromFiles $*
