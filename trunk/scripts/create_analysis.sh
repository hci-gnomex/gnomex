#!/bin/bash
CLASSPATH="$CLASSPATH:./gnomex.jar:../../lib/jdom.jar"
export CLASSPATH
java hci.gnomex.utility.CreateAnalysisMain $*