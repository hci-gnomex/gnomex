#!/usr/bin/env bash
tomcatScriptPath="/usr/share/apache-tomcat-7.0.79/webapps/gnomex/scripts/"
scriptsPath="/home/u0566434/Scripts/"
pDataPath="/home/u0566434/parser_data/"
fScriptsPath=$scriptsPath"foundation/" # where the scripts live
remoteDataPath="/mnt/win/Results/"   #"/home/u0566434/FoundationData/"
foundationPath="/Repository/PersonData/2017/2R/Foundation/"

regex="^(.*RF[0-9]+)(\.|_).*"  #"^(TRF[0-9]+)_(\wNA)(.*)" # Match TRF and its type (DNA/RNA) and then the file extension
regex="^.*([CQT]RF[0-9]+)(.|_)(.*)"

TOMCAT_HOME=../../../
COMMON_LIB=$TOMCAT_HOME/lib

GNOMEX_LIB=../WEB-INF/lib
GNOMEX_CLASSES=../WEB-INF/classes

CLASSPATH=".:$GNOMEX_CLASSES:"

for JAR in $COMMON_LIB/*.jar
do
    CLASSPATH="$CLASSPATH:$JAR"
done

for JAR in $GNOMEX_LIB/*.jar
do
    CLASSPATH="$CLASSPATH:$JAR"
done
export CLASSPATH

export CLASSPATH


set -e

flaggedIDParam=${1:-normal}
idColumn=${2:-1}
fileList=""
verifiedTrfInfo=""
idStr=""

#java hci.gnomex.daemon.auto_import.FileMover -accountFilesMoved $foundationPath
tree "$foundationPath" --noreport > "$pDataPath"localFoundationTree.out
java hci.gnomex.daemon.auto_import.PathMaker "$pDataPath"localFoundationTree.out "$pDataPath"localFoundationPath.out
ls $remoteDataPath*RF* > "$pDataPath"remoteFoundationPath.out
java hci.gnomex.daemon.auto_import.DiffParser  "$pDataPath"localFoundationPath.out  "$pDataPath"remoteFoundationPath.out > "$pDataPath"uniqueFilesToMove.out
if [ "$flaggedIDParam" = "normal"  ]; then

    bash "$scriptsPath"makeReattemptList.sh $remoteDataPath ".*RF.*"  "$pDataPath"reattemptFileList.out
    # Filter file order of types  params: in, out, path, out, out
    java hci.gnomex.daemon.auto_import.FilterFile "$pDataPath"uniqueFilesToMove.out "$pDataPath"fileList.out "$pDataPath"hci-creds.properties "$pDataPath"filteredOutList.out
    #java hci.gnomex.daemon.auto_import.DiffParser "$pDataPath"localChecksums.out  "$pDataPath"remoteChecksums.out "$pDataPath"

    #cat "$pDataPath"inclusion.out "$pDataPath"reattemptFileList.out "$pDataPath"fileList.out > "$pDataPath"importableFileList.out
    cat "$pDataPath"reattemptFileList.out "$pDataPath"fileList.out > "$pDataPath"importableFileList.out
    fileList="$pDataPath"importableFileList.out

    #cat "$pDataPath"remoteChecksums.out
    #cat "$pDataPath"localChecksums.out
    #rm "$pDataPath"*Checksums.out
else
    # occur when user has verified the data and wants to intervene by supplying a file to use over database query
    find $remoteDataPath"Flagged/" -regextype sed -regex ".*RF.*"  -printf '%f\n' > $remoteDataPath"Flagged/"tempFilesToVerify.out
    cat "$pDataPath"uniqueFilesToVerify.out
    bash "$scriptsPath"makeVerifiedList.sh $flaggedIDParam  $remoteDataPath"Flagged/"tempFilesToVerify.out "$pDataPath"verifiedFoundList.out $idColumn
    fileList="$pDataPath"verifiedFoundList.out
    echo else
fi

echo hello out there from foundation

while read fileName; do
        if [[ $fileName =~ $regex ]]; then
                id=${BASH_REMATCH[1]}
                fullMatch=$BASH_REMATCH

                #fileSeqType="${BASH_REMATCH[2]}"

                if [ ! -z "$id" ]; then # If id variable is not empty
                        idStr+=$id","
                fi
                                #echo $id
        fi
done < $fileList # has all new file names to import plus reattempts files
echo $idStr | java  hci.gnomex.daemon.auto_import.StringModder > "$pDataPath"tempStr.out
echo The temp str:

cat "$pDataPath"tempStr.out


if [ "$flaggedIDParam" = "normal"  ]; then
    java  hci.gnomex.daemon.auto_import.Linker "$pDataPath"tempStr.out "$pDataPath"hci-creds.properties "$pDataPath"trfInfo.out foundation
    verifiedTrfInfo="$pDataPath"trfInfo.out
else
    verifiedTrfInfo=$flaggedIDParam
fi



rm "$pDataPath"tempStr.out

java hci.gnomex.daemon.auto_import.XMLParserMain -file $verifiedTrfInfo -initXML "$pDataPath"clinRequest.xml -annotationXML "$pDataPath"clinGetPropertyList.xml -importScript import_experiment.sh -outFile "$pDataPath"tempRequest.xml -importMode foundation
java hci.gnomex.daemon.auto_import.FileMover -file $fileList  -root $foundationPath -downloadPath $remoteDataPath -flaggedFile "$pDataPath"flaggedIDs.out -mode foundation
#rm "$pDataPath"tempAnalysisList.out
echo ------------------------------------------------------------------------------------------------------------
#cat matched.txt | java -jar fileNavParser.jar
#rm ../Data/matched.txt
#rm catList.txt
