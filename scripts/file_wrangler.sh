#!/usr/bin/env bash
foundStatus=1
avatarStatus=0
pDataPath="/home/u0566434/parser_data/"


#bash avatarWrangler.sh &> avatar.log
avatarStatus=$?

avaRequestList=`cat "$pDataPath"tempRequestList.out`
avaAnalysisList=`cat "$pDataPath"tempAnalysisList.out`


rm "$pDataPath"tempRequestList.out
rm "$pDataPath"tempAnalysisList.out

#bash foundationWrangler.sh &> foundation.log
#foundStatus=$?
#foundRequestList=`cat "$pDataPath"tempRequestList.out`
#foundAnalysisList=`cat "$pDataPath"tempAnalysisList.out`


#rm "$pDataPath"tempRequestList.out
#rm "$pDataPath"tempAnalysisList.out


echo foundation status: $foundStatus
echo avatar status: $avatarStatus

echo here is the output: $output

if [ $foundStatus -eq 0 ] && [ $avatarStatus -eq 0 ]; then
    echo importing was successful
    bash register_files.sh -all -doNotSendMail

    bash LinkData.sh -dataSource 2R -requests $foundRequestList
    bash LinkData.sh -dataSource 4R -requests $avaRequestList

    bash LinkFastqData.sh -debug -analysis $foundAnalysisList
    bash LinkFastqData.sh -debug -analysis $avaAnalysisList

    bash index_gnomex.sh


else
:<<'END'
    if [ $foundStatus -eq 0 ]; then
        bash register_files.sh -all -doNotSendMail
        bash linkData.sh -dataSource 2R -requests $foundRequestList
        bash linkFastqData.sh -debug -analysis $foundAnalysisList
        bash index_gnomex.sh
    else
        echo need to notify by email foundation failed importing
    fi
END
    if [ $avatarStatus -eq 0 ]; then
        bash register_files.sh -all -doNotSendMail
        bash linkData.sh -dataSource 4R -requests $avaRequestList
        bash linkFastqData.sh -debug -analysis $avaAnalysisList
        bash index_gnomex.sh
    else
        echo need to notify by email avatar failed importing
    fi


fi
