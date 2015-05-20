#!/bin/bash
CLASSPATH="$CLASSPATH:../WEB-INF/lib/gnomex_client.jar"
export CLASSPATH
java hci.gnomex.httpclient.SaveChromatogramsFromFiles $*
