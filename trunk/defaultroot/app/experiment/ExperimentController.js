'use strict';

/**
 * ExperimentController
 * @constructor
 */

var experiment    = angular.module('experiment', ['ui.bootstrap','treeControl','treeGrid', 'filters', 'services', 'directives','chosen','ngProgress','dialogs.main','error']);
console.log("In ExperimentController.js");

angular.module("experiment")
  .factory('ProjectRequestListService', function($http) {
    return {
      getProjects: function(whatToGet) {
         return $http.get('/gnomexlite/GetProjectRequestList.gx' + whatToGet);
      }
    }
  })
  .factory('RequestService', function($http) {
    return {
      getRequest: function(exprid) {
         return $http.get('/gnomexlite/GetRequest.gx?requestNumber=' + exprid);
      }
    }
  })
  .factory('RequestDownloadListService', function($http) {
    return {
      getRequestDownloadList: function(exprid) {
         return $http.get('/gnomexlite/GetRequestDownloadList.gx?requestNumber=' + exprid);
      }
    }
  })

.controller("ExperimentController", [
'$scope', '$http', '$modal','$rootScope','$q','$location','ngProgress','dialogs','RequestService','ProjectRequestListService','RequestDownloadListService',
function($scope, $http, $modal, $rootScope, $q, $location, ngProgress, dialogs, RequestService, ProjectRequestListService, RequestDownloadListService) {
	/**********************
	 * Initialization!
	 *********************/
console.log("In ExperimentController.js 2, $rootScope.whatToLookup: " + $rootScope.whatToLookup);
$scope.xmlExperiment = "";
$scope.projectRequestList = "";
$scope.okToFind = true;				// comes from $rootScope
$scope.okToPickLab = true;			// comes from $rootScope
$scope.showColumnForm = true;
$scope.whatToLookup = $rootScope.whatToLookup;

$scope.searchFilter = {selectAll: false, group: "", owner: "", lookup: ""};

$scope.labs = [];					// comes from $rootScope
$scope.experimentTree = [];
$scope.expandedExperimentTree = [];

$scope.xmlFiles = "";
$scope.etree_data = [];
$scope.eexpanding_property = {field: "file", displayName: "File or Folder Name"};

$scope.ecol_defs = [
	{field: "view", displayName: "View", cellTemplate: '<div ng-show="{{ row.branch[col.field] }}"><a ng-href="{{ row.branch[col.field] }}">view</a></div>'},
	{field: "info", displayName: "Info"},
	{field: "linkedSample", displayName: "Linked Sample"},
	{field: "size", displayName: "Size"},
	{field: "modified", displayName: "Modified"}];

// fake data
//$scope.reqDownloadList = {"Request":{"displayName":"11643R","requestNumber":"11643R","idRequest":"26761","codeRequestCategory":"HISEQ","icon":"assets/DNA_diag_lightening.png","isSelected":"false","state":"unchecked","isEmpty":"N","canDelete":"N","canRename":"N","info":"Watkins, Scott","RequestDownload":[{"key":"2015 20150602 11643R Fastq 1","isSelected":"N","state":"unchecked","altColor":"true","showRequestNumber":"Y","idRequest":"26761","createDate":"06/02/2015","requestNumber":"11643R","codeRequestCategory":"HISEQ","codeApplication":"TDNASEQ","idAppUser":"839","itemNumber":"Fastq","hybDate":"","extractionDate":"","hybFailed":"","labelingDateSample1":"","qualDateSample1":"","numberSample1":"","nameSample1":"","labelingDateSample2":"","qualDateSample2":"","numberSample2":"","nameSample2":"","idLab":"294","canDelete":"Y","canRename":"Y","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq","ownerFirstName":"Scott","ownerLastName":"Watkins","appUserName":"Watkins, Scott","results":"","hasResults":"Y","status":"","isEmpty":"N","FileDescriptor":[{"zipEntryName":"11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","lastModifyDate":"20150611T082010","fileSize":"9148026213","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"9  gb","flowCellIndicator":"","isSelected":"N","childFileSize":"9148026213","requestNumber":"11643R","viewURL":"","displayName":"11643X1_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","lastModifyDate":"20150611T084218","fileSize":"83","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"83 b","flowCellIndicator":"","isSelected":"N","childFileSize":"83","requestNumber":"11643R","viewURL":"","displayName":"11643X1_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","lastModifyDate":"20150611T082026","fileSize":"9442121485","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"9  gb","flowCellIndicator":"","isSelected":"N","childFileSize":"9442121485","requestNumber":"11643R","viewURL":"","displayName":"11643X1_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","lastModifyDate":"20150611T084219","fileSize":"83","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"83 b","flowCellIndicator":"","isSelected":"N","childFileSize":"83","requestNumber":"11643R","viewURL":"","displayName":"11643X1_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X1_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","lastModifyDate":"20150611T082911","fileSize":"6928788453","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"6  gb","flowCellIndicator":"","isSelected":"N","childFileSize":"6928788453","requestNumber":"11643R","viewURL":"","displayName":"11643X2_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","lastModifyDate":"20150611T084219","fileSize":"83","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"83 b","flowCellIndicator":"","isSelected":"N","childFileSize":"83","requestNumber":"11643R","viewURL":"","displayName":"11643X2_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","lastModifyDate":"20150611T083039","fileSize":"7061843480","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"7  gb","flowCellIndicator":"","isSelected":"N","childFileSize":"7061843480","requestNumber":"11643R","viewURL":"","displayName":"11643X2_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","lastModifyDate":"20150611T084219","fileSize":"83","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"83 b","flowCellIndicator":"","isSelected":"N","childFileSize":"83","requestNumber":"11643R","viewURL":"","displayName":"11643X2_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X2_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","lastModifyDate":"20150611T083705","fileSize":"6638286066","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"6  gb","flowCellIndicator":"","isSelected":"N","childFileSize":"6638286066","requestNumber":"11643R","viewURL":"","displayName":"11643X3_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","lastModifyDate":"20150611T084219","fileSize":"83","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"83 b","flowCellIndicator":"","isSelected":"N","childFileSize":"83","requestNumber":"11643R","viewURL":"","displayName":"11643X3_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_1.txt.gz.md5","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","lastModifyDate":"20150611T083905","fileSize":"6832194881","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"6  gb","flowCellIndicator":"","isSelected":"N","childFileSize":"6832194881","requestNumber":"11643R","viewURL":"","displayName":"11643X3_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""},{"zipEntryName":"11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","lastModifyDate":"20150611T084219","fileSize":"83","lastModifyDateDisplay":"2015-06-11","directoryName":"Fastq","fileSizeText":"83 b","flowCellIndicator":"","isSelected":"N","childFileSize":"83","requestNumber":"11643R","viewURL":"","displayName":"11643X3_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","fileName":"/Repository/MicroarrayData/2015/11643R/Fastq/11643X3_150605_D00550_0260_BC6V22ANXX_8_2.txt.gz.md5","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""}]},{"key":"2015 20150605 11643R FC641 1 FC","isSelected":"N","state":"unchecked","altColor":"true","idRequest":"26761","createDate":"06/02/2015","requestNumber":"11643R","codeRequestCategory":"HISEQ","codeApplication":"TDNASEQ","idAppUser":"839","idLab":"294","results":"flow cell quality report","hasResults":"Y","status":"","canDelete":"N","canRename":"N","itemNumber":"FC641","isEmpty":"N","FileDescriptor":{"zipEntryName":"11643R/FC641/barcode_report_150605_D00550_0260_BC6V22ANXX.xls","lastModifyDate":"20150611T110014","fileSize":"15319","lastModifyDateDisplay":"2015-06-11","directoryName":"FC641","fileSizeText":"15  kb","flowCellIndicator":"FC","isSelected":"N","childFileSize":"15319","requestNumber":"11643R","viewURL":"DownloadSingleFileServlet.gx?requestNumber=11643R&fileName=barcode_report_150605_D00550_0260_BC6V22ANXX.xls&view=Y&dir=FC641","displayName":"barcode_report_150605_D00550_0260_BC6V22ANXX.xls","fileName":"/Repository/FlowCellData/2015/FC641/barcode_report_150605_D00550_0260_BC6V22ANXX.xls","number":"11643R","canDelete":"N","canRename":"N","state":"unchecked","linkedSampleNumber":""}},{"key":"2015 20150602 11643R Library QC 1","isSelected":"N","state":"unchecked","altColor":"true","showRequestNumber":"N","idRequest":"26761","createDate":"06/02/2015","requestNumber":"11643R","codeRequestCategory":"HISEQ","codeApplication":"TDNASEQ","idAppUser":"839","itemNumber":"Library QC","hybDate":"","extractionDate":"","hybFailed":"","labelingDateSample1":"","qualDateSample1":"","numberSample1":"","nameSample1":"","labelingDateSample2":"","qualDateSample2":"","numberSample2":"","nameSample2":"","idLab":"294","canDelete":"Y","canRename":"Y","fileName":"/Repository/MicroarrayData/2015/11643R/Library QC","ownerFirstName":"Scott","ownerLastName":"Watkins","appUserName":"Watkins, Scott","results":"","hasResults":"Y","status":"","isEmpty":"N","FileDescriptor":{"zipEntryName":"11643R/Library QC/11643 CL1-3_LibraryTapeStation.pdf","lastModifyDate":"20150605T093801","fileSize":"355512","lastModifyDateDisplay":"2015-06-05","directoryName":"Library QC","fileSizeText":"347  kb","flowCellIndicator":"","isSelected":"N","childFileSize":"355512","requestNumber":"11643R","viewURL":"DownloadSingleFileServlet.gx?requestNumber=11643R&fileName=11643 CL1-3_LibraryTapeStation.pdf&view=Y&dir=Library QC","displayName":"11643 CL1-3_LibraryTapeStation.pdf","fileName":"/Repository/MicroarrayData/2015/11643R/Library QC/11643 CL1-3_LibraryTapeStation.pdf","number":"11643R","canDelete":"Y","canRename":"Y","state":"unchecked","linkedSampleNumber":""}},{"key":"2015 20150602 11643R Sample QC 1","isSelected":"N","state":"unchecked","altColor":"true","showRequestNumber":"N","idRequest":"26761","createDate":"06/02/2015","requestNumber":"11643R","codeRequestCategory":"HISEQ","codeApplication":"TDNASEQ","idAppUser":"839","itemNumber":"Sample QC","hybDate":"","extractionDate":"","hybFailed":"","labelingDateSample1":"","qualDateSample1":"","numberSample1":"","nameSample1":"","labelingDateSample2":"","qualDateSample2":"","numberSample2":"","nameSample2":"","idLab":"294","canDelete":"Y","canRename":"Y","fileName":"/Repository/MicroarrayData/2015/11643R/Sample QC","ownerFirstName":"Scott","ownerLastName":"Watkins","appUserName":"Watkins, Scott","results":"","hasResults":"Y","status":"","isEmpty":"Y"}]}};
$scope.reqDownloadList = {"Request": {  "displayName": "86R1","requestNumber": "86R1","idRequest": "86","codeRequestCategory": "HISEQ","icon": "assets/DNA_diag_lightening.png","isSelected": "false","state": "unchecked","isEmpty": "N","canDelete": "N","canRename": "N","info": "Bruneau,Benoit","FileDescriptor":   [        {      "zipEntryName": "86R/Baf60c_het.bam","childFileSize": "2045205434","lastModifyDate": "20120530T213503","fileSize": "2045205434","lastModifyDateDisplay": "2012-05-30","flowCellIndicator": "","fileSizeText": "2  gb","requestNumber": "86R1","isSelected": "N","viewURL": "","directoryName": "","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Baf60c_het.bam","number": "86R1","displayName": "Baf60c_het.bam","state": "unchecked","canDelete": "Y","canRename": "Y","linkedSampleNumber": ""    },{      "zipEntryName": "86R/Baf60c_mut.bam","childFileSize": "2313884279","lastModifyDate": "20120530T220732","fileSize": "2313884279","lastModifyDateDisplay": "2012-05-30","flowCellIndicator": "","fileSizeText": "2  gb","requestNumber": "86R1","isSelected": "N","viewURL": "","directoryName": "","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Baf60c_mut.bam","number": "86R1","displayName": "Baf60c_mut.bam","state": "unchecked","canDelete": "Y","canRename": "Y","linkedSampleNumber": ""    },{      "zipEntryName": "86R/fastqc_report.html","childFileSize": "96237","lastModifyDate": "20140721T121806","fileSize": "96237","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "94  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=fastqc_report.html&view=Y","directoryName": "","fileName": "/Data01/gnomex/ExperimentData/2012/86R/fastqc_report.html","number": "86R1","displayName": "fastqc_report.html","state": "unchecked","canDelete": "Y","canRename": "Y","linkedSampleNumber": ""    }  ],"RequestDownload":   [        {      "key": "2012 20120601 86R1 Images 1","isSelected": "N","state": "unchecked","altColor": "true","showRequestNumber": "Y","idRequest": "86","createDate": "06/01/2012","requestNumber": "86R1","codeRequestCategory": "HISEQ","codeApplication": "MRNASEQ","idAppUser": "39","itemNumber": "Images","hybDate": "","extractionDate": "","hybFailed": "","labelingDateSample1": "","qualDateSample1": "","numberSample1": "","nameSample1": "","labelingDateSample2": "","qualDateSample2": "","numberSample2": "","nameSample2": "","idLab": "13","canDelete": "Y","canRename": "Y","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images","ownerFirstName": "Benoit","ownerLastName": "Bruneau","appUserName": "Bruneau,Benoit","results": "","hasResults": "Y","status": "","isEmpty": "N","FileDescriptor":       [                {          "zipEntryName": "86R/Images/duplication_levels.png","childFileSize": "40552","lastModifyDate": "20140721T121806","fileSize": "40552","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "40  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=duplication_levels.png&view=Y&dir=Images","directoryName": "Images","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images/duplication_levels.png","number": "86R1","displayName": "duplication_levels.png","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": ""        },    {          "zipEntryName": "86R/Images/per_base_gc_content.png","childFileSize": "42545","lastModifyDate": "20140721T121806","fileSize": "42545","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "42  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=per_base_gc_content.png&view=Y&dir=Images","directoryName": "Images","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images/per_base_gc_content.png","number": "86R1","displayName": "per_base_gc_content.png","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": ""        },    {          "zipEntryName": "86R/Images/per_base_n_content.png","childFileSize": "16523","lastModifyDate": "20140721T121806","fileSize": "16523","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "16  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=per_base_n_content.png&view=Y&dir=Images","directoryName": "Images","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images/per_base_n_content.png","number": "86R1","displayName": "per_base_n_content.png","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": ""        },    {          "zipEntryName": "86R/Images/per_base_quality.png","childFileSize": "32378","lastModifyDate": "20140721T121806","fileSize": "32378","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "32  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=per_base_quality.png&view=Y&dir=Images","directoryName": "Images","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images/per_base_quality.png","number": "86R1","displayName": "per_base_quality.png","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": ""        },    {          "zipEntryName": "86R/Images/per_sequence_gc_content.png","childFileSize": "47726","lastModifyDate": "20140721T121806","fileSize": "47726","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "47  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=per_sequence_gc_content.png&view=Y&dir=Images","directoryName": "Images","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images/per_sequence_gc_content.png","number": "86R1","displayName": "per_sequence_gc_content.png","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": ""        },    {          "zipEntryName": "86R/Images/per_sequence_quality.png","childFileSize": "31946","lastModifyDate": "20140721T121806","fileSize": "31946","lastModifyDateDisplay": "2014-07-21","flowCellIndicator": "","fileSizeText": "31  kb","requestNumber": "86R1","isSelected": "N","viewURL": "DownloadSingleFileServlet.gx?requestNumber=86R1&fileName=per_sequence_quality.png&view=Y&dir=Images","directoryName": "Images","fileName": "/Data01/gnomex/ExperimentData/2012/86R/Images/per_sequence_quality.png","number": "86R1","displayName": "per_sequence_quality.png","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": ""        }      ]    },{      "key": "2012 20120601 86R1 fastq 1","isSelected": "N","state": "unchecked","altColor": "true","showRequestNumber": "N","idRequest": "86","createDate": "06/01/2012","requestNumber": "86R1","codeRequestCategory": "HISEQ","codeApplication": "MRNASEQ","idAppUser": "39","itemNumber": "fastq","hybDate": "","extractionDate": "","hybFailed": "","labelingDateSample1": "","qualDateSample1": "","numberSample1": "","nameSample1": "","labelingDateSample2": "","qualDateSample2": "","numberSample2": "","nameSample2": "","idLab": "13","canDelete": "Y","canRename": "Y","fileName": "/Data01/gnomex/ExperimentData/2012/86R/fastq","ownerFirstName": "Benoit","ownerLastName": "Bruneau","appUserName": "Bruneau,Benoit","results": "","hasResults": "Y","status": "","isEmpty": "N","FileDescriptor":       [                {          "zipEntryName": "86R/fastq/120507-FC412-L5-CGATGT--Bruneau--BL6_Baf60c_het--Mm--R1.fq","childFileSize": "5773434361","lastModifyDate": "20120521T084932","fileSize": "5773434361","lastModifyDateDisplay": "2012-05-21","flowCellIndicator": "","fileSizeText": "5  gb","requestNumber": "86R1","isSelected": "N","viewURL": "","directoryName": "fastq","fileName": "/Data01/gnomex/ExperimentData/2012/86R/fastq/120507-FC412-L5-CGATGT--Bruneau--BL6_Baf60c_het--Mm--R1.fq","number": "86R1","displayName": "120507-FC412-L5-CGATGT--Bruneau--BL6_Baf60c_het--Mm--R1.fq","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": "86X1"        },    {          "zipEntryName": "86R/fastq/120507-FC412-L5-CGATGT--Bruneau--BL6_Baf60c_het--Mm--R2.fq","childFileSize": "5773434361","lastModifyDate": "20120521T084935","fileSize": "5773434361","lastModifyDateDisplay": "2012-05-21","flowCellIndicator": "","fileSizeText": "5  gb","requestNumber": "86R1","isSelected": "N","viewURL": "","directoryName": "fastq","fileName": "/Data01/gnomex/ExperimentData/2012/86R/fastq/120507-FC412-L5-CGATGT--Bruneau--BL6_Baf60c_het--Mm--R2.fq","number": "86R1","displayName": "120507-FC412-L5-CGATGT--Bruneau--BL6_Baf60c_het--Mm--R2.fq","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": "86X1"        },    {          "zipEntryName": "86R/fastq/120507-FC412-L5-TAGCTT--Bruneau--BL6_Baf60c_mut--Mm--R1.fq","childFileSize": "7130751600","lastModifyDate": "20120521T085324","fileSize": "7130751600","lastModifyDateDisplay": "2012-05-21","flowCellIndicator": "","fileSizeText": "7  gb","requestNumber": "86R1","isSelected": "N","viewURL": "","directoryName": "fastq","fileName": "/Data01/gnomex/ExperimentData/2012/86R/fastq/120507-FC412-L5-TAGCTT--Bruneau--BL6_Baf60c_mut--Mm--R1.fq","number": "86R1","displayName": "120507-FC412-L5-TAGCTT--Bruneau--BL6_Baf60c_mut--Mm--R1.fq","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": "86X2"        },    {          "zipEntryName": "86R/fastq/120507-FC412-L5-TAGCTT--Bruneau--BL6_Baf60c_mut--Mm--R2.fq","childFileSize": "7130751600","lastModifyDate": "20120521T085325","fileSize": "7130751600","lastModifyDateDisplay": "2012-05-21","flowCellIndicator": "","fileSizeText": "7  gb","requestNumber": "86R1","isSelected": "N","viewURL": "","directoryName": "fastq","fileName": "/Data01/gnomex/ExperimentData/2012/86R/fastq/120507-FC412-L5-TAGCTT--Bruneau--BL6_Baf60c_mut--Mm--R2.fq","number": "86R1","displayName": "120507-FC412-L5-TAGCTT--Bruneau--BL6_Baf60c_mut--Mm--R2.fq","canDelete": "Y","canRename": "Y","state": "unchecked","linkedSampleNumber": "86X2"        }      ]    },{      "key": "2012 20120601 86R1 standard_analysis_files 1","isSelected": "N","state": "unchecked","altColor": "true","showRequestNumber": "N","idRequest": "86","createDate": "06/01/2012","requestNumber": "86R1","codeRequestCategory": "HISEQ","codeApplication": "MRNASEQ","idAppUser": "39","itemNumber": "standard_analysis_files","hybDate": "","extractionDate": "","hybFailed": "","labelingDateSample1": "","qualDateSample1": "","numberSample1": "","nameSample1": "","labelingDateSample2": "","qualDateSample2": "","numberSample2": "","nameSample2": "","idLab": "13","canDelete": "Y","canRename": "Y","fileName": "/Data01/gnomex/ExperimentData/2012/86R/standard_analysis_files","ownerFirstName": "Benoit","ownerLastName": "Bruneau","appUserName": "Bruneau,Benoit","results": "","hasResults": "Y","status": "","isEmpty": "Y"    }  ]}};

$scope.selectedDescription = {};

$scope.selectedSampleLabelsIL = [];
$scope.selectedSamplesIL = [];

$scope.selectedExperDesignLabelsIL = [];
$scope.selectedExperDesignIL = [];

$scope.selectedSequenceLabelsIL = [];
$scope.selectedSequencesIL = [];

$scope.experimentEditMode = false;
$scope.labSelected = "";
$scope.projectSelected = "";
$scope.numberOfExperiments = 0;
$scope.experiments = [];
$scope.projects = [];
$scope.experimentList = 'app/experiment/experimentList.html';
$scope.experimentDetailIL = 'app/experiment/experimentDetailIL.html';
$scope.experimentDetailEIL = 'app/experiment/experimentDetailEIL.html';
$scope.experimentDetailHS = 'app/experiment/experimentDetailHS.html';
$scope.experimentDetailDNA = 'app/experiment/experimentDetailDNA.html';
$scope.experimentForm = $scope.experimentList;
$scope.experimentSelected = null;
$scope.experimentSelectedIL = null;
$scope.visibilitySelected = "All Lab Members";
$scope.institutionSelected = "";
$scope.headingSelected = "";

$scope.downloadList = [];

// start the world
activate();

$scope.findAll = function() {
	//console.log("[findAll] toggle $scope.searchFilter.selectAll: " + $scope.searchFilter.selectAll);
	$scope.searchFilter.selectAll = !$scope.searchFilter.selectAll;
	$scope.searchFilter.group = "";
	$scope.searchFilter.lookup = "";
	$scope.showColumnForm = true;
};

$scope.find = function() {
	//console.log("[find] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group + " lookup: " + $scope.searchFilter.lookup);
	$scope.showColumnForm = true;

	if ($scope.searchFilter.lookup != "") {
		// *** will never be executed ***
		// get the experiment, if it exists, setup the experimentTree for the lab and display the experiment
		var exprid = mapLookup($scope.searchFilter.lookup);
		lookupExperiment(exprid);
	} else {

    	// do they want all of them?
    	var whatToGet = "?allExperiments=Y";
    	if ($scope.searchFilter.selectAll) {
			whatToGet = "?allExperiments=Y";
		}
		else {
			// specific lab
			if (angular.isDefined($scope.searchFilter.group.name)) {
				//console.log("[find] looking for " + $scope.searchFilter.group.name);
				// get the lab id
				for (var i = 0; i<$scope.labs.length; i++) {
					if ($scope.searchFilter.group.name == $scope.labs[i].name) {
						whatToGet = "?idLab=" + $scope.labs[i].idLab;
						break;
					}
				}
			}
		}

		//console.log("[find] *** calling getProjectRequestList, whatToGet: " + whatToGet);
		getProjectRequestList(whatToGet);
	} // end of else
};

$scope.lookUp = function() {
	//console.log("[lookUp] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group + " lookup: " + $scope.searchFilter.lookup);

	// get the experiment, if it exists, setup the experimentTree for the lab and display the experiment
	var exprid = mapLookup($scope.searchFilter.lookup);
	lookupExperiment(exprid);
};

 $scope.$on('lookupExperiment', function (event, args) {
	 $scope.searchFilter.lookup = args.message;
	 var exprid = mapLookup($scope.searchFilter.lookup);
	 console.log("[$on lookupExperiment event] $scope.searchFilter.lookup " + $scope.searchFilter.lookup + " exprid: " + exprid);

	 // do the work
	 lookupExperiment(exprid);
 });

 	/*********************
 	 *
 	 * Watchers
 	 *
 	 ********************/
     $scope.$watch('whatToLookup',function() {
		console.log("[whatToLookup watcher] $scope.whatToLookup: " + $scope.whatToLookup);
     	if ($scope.whatToLookup != null && $scope.whatToLookup != "") {
			$scope.searchFilter.lookup = $scope.whatToLookup;
			var exprid = mapLookup($scope.searchFilter.lookup);
			lookupExperiment(exprid);
     	}
     });


$scope.showSelected = function(sel) {
		//console.log("In showSelected " + sel.type + " " + sel.label + " " + $scope.experimentForm);
		$scope.showColumnForm = true;

         $scope.selectedNode = sel;
         if (sel.type == "L") {
		 	$scope.labSelected = sel.label;
		 	$scope.headingSelected = $scope.labSelected;
		 	$scope.experiments = [];      // sel.children[1].children;

		 	$scope.projects = angular.copy(sel.children);

			// Go through each project and collect all the experiments
			//console.log("$scope.projects.length " + $scope.projects.length);
			for (var i=0; i<$scope.projects.length; i++) {
				//console.log("Processing project: " + $scope.projects[i].label);
				for (var j=0; j<$scope.projects[i].children.length; j++) {
					//console.log("Processing child: " + $scope.projects[i].children[j].label);
					$scope.experiments.push($scope.projects[i].children[j]);
				}
			}

		 	$scope.numberOfExperiments = $scope.experiments.length;
		 	$scope.experimentForm = $scope.experimentList;
		}
		if (sel.type == "P") {
			$scope.projectSelected = sel.label;
		 	$scope.headingSelected = $scope.projectSelected;
			$scope.experiments = angular.copy(sel.children);
			$scope.numberOfExperiments = $scope.experiments.length;
			$scope.experimentForm = $scope.experimentList;
		}
		if (sel.type == "E") {
			$scope.experimentSelected = angular.copy(sel);
			$scope.experiments = angular.copy(sel);
			$scope.numberOfExperiments = $scope.experiments.length;
			if (sel.etype == "IL") {
			$scope.experimentForm = $scope.experimentDetailIL;
			}
			if (sel.etype == "EIL") {
			$scope.experimentForm = $scope.experimentDetailEIL;
			}
			if (sel.etype == "HS") {
			$scope.experimentForm = $scope.experimentDetailHS;
			}
			if (sel.etype == "DNA") {
			$scope.experimentForm = $scope.experimentDetailDNA;
			}

			getFiles(sel.number);
			getExperiment(sel.number);
			//console.log("In showSelected, label: " + $scope.experimentSelected.label + " number: " + $scope.experimentSelected.number + " etype: " + $scope.experimentSelected.etype);
		}
		//console.log("In showSelected, form: " + $scope.experimentForm);
     };

     $scope.efileSelectedHandler = function(branch) {
		console.log("In efileSelectedHandler " + branch.file);
	};

	function activate() {

		// get what we need from the $rootScope
		$scope.labs = angular.copy($rootScope.labs);
		console.log("[ExperimentController activate] $scope.labs.length: " + $scope.labs.length);
		$scope.okToPickLab = angular.copy($rootScope.okToPickLab);
		console.log("[ExperimentController activate] $scope.okToPickLab: " + $scope.okToPickLab);
		$scope.okToFind = angular.copy($rootScope.okToFind);
		console.log("[ExperimentController activate] $scope.okToFind: " + $scope.okToFind);
	};

     function getProjectRequestList(whatToGet) {
       var promise = ProjectRequestListService.getProjects(whatToGet);

       promise.then(
          function(payload) {
              $scope.projectRequestList = payload.data;

              // setup the experiment tree
              buildExperimentTree();
          },
          function(errorPayload) {
              console.log('failure getProjectRequestList' + errorPayload);
          });
     };

     function getExperiment(exprid) {
       var promise = RequestService.getRequest(exprid);

       promise.then(
          function(payload) {
              $scope.xmlExperiment = payload.data;

              // parse out what we need for the screens
              parseExperiment();
          },
          function(errorPayload) {
              console.log('failure getting Experiment' + errorPayload);
          });
     };

     function lookupExperiment(exprid) {
		 //console.log("[lookupExperiment] ** before getRequest ** exprid: " + exprid);
       var promise = RequestService.getRequest(exprid);

       promise.then(
          function(payload) {
              $scope.xmlExperiment = payload.data;

			  // don't display the initial column form list of experiment
			  $scope.showColumnForm = false;

			  // did we get anything?
			  if (angular.isDefined($scope.xmlExperiment.Request) ) {
				  // get the files
			  	  getFiles(exprid);

              	  // parse out what we need for the screens
              	  parseExperiment();

              	  var idLab = null;

              	  // did we get the experiment?
              	  if (angular.isDefined($scope.xmlExperiment.Request.idLab)) {
				  	  idLab = $scope.xmlExperiment.Request.idLab;
			  	  }

			 	  if (idLab != null) {
              	  	// get the project request list for the experiments lab
              	  	var whatToGet = "?idLab=" + idLab;
              	  	getProjectRequestList(whatToGet);
		    	  }
			  } // end of if we got something good

          },
          function(errorPayload) {
              console.log('failure looking up Experiment' + errorPayload);
          });
     };

     function getFiles(exprid) {
       var promise = RequestDownloadListService.getRequestDownloadList(exprid);

       promise.then(
          function(payload) {
              $scope.xmlFiles = payload.data;

              // *** note *** for now we just fake it
              //$scope.xmlFiles = angular.copy($scope.reqDownloadList);

              // parse out what we need for the Files tab
              parseFiles();
          },
          function(errorPayload) {
              console.log('failure getting RequestDownloadList' + errorPayload);
          });
     };

	function mapLookup(requestNumber) {
		var exprid = requestNumber;
		if (requestNumber == null || requestNumber == "") {
			//console.log("[mapLookup] 1 returning exprid: " + exprid);
			return exprid;
		}

		var spos = -1;
		var epos = -1;
		for (var i=0;i<requestNumber.length;i++) {
			var char = requestNumber.charAt(i);
			if (char <= "9") {
				if (spos == -1) {
					spos = i;
				}
				continue;
			}
			if (spos == -1) {
				continue;
			}
			epos = i;
			break;

		} // end of for

		// did we find anything valid?
		if (spos == -1) {
			//console.log("[mapLookup] 2 returning exprid: " + exprid);
			return exprid;		// nope
		}

		if (epos == -1) {
			exprid = requestNumber.substring(spos);
		}
		else {
			exprid = requestNumber.substring(spos,epos);
		}

		//console.log("[mapLookup] 3 returning exprid: " + exprid + " requestNumber: " + requestNumber + " spos: " + spos + " epos: " + epos);
		return exprid;

	}; // end of mapLookup


    function parseText (text) {
		var prettyString = "";

		if (text == null || text == "") {
			return text;
		}

		var spos = 0;
		var epos = -1;
		var tlength = text.length;
		var numfound = 0;
		while (spos <= tlength) {
			epos = text.indexOf("<P ALIGN=\"LEFT\">",spos);

			// first time and not found then nothing to do
			if (epos == -1) {
				if (numfound == 0) {
					prettyString = text;
					break;
				}

				prettyString += text.substring(spos);
				break;
			}

			numfound++;
			spos = epos + 16;
			if (spos >= tlength) {
				break;
			}

			epos = text.indexOf("</P>",spos);
			if (epos == -1) {
				prettyString += text.substring(spos);
				break;
			}

			if (epos == spos) {
				// just add a newline
				prettyString += "\n";
				spos = epos + 4;
				continue;
			}

			prettyString += text.substring(spos,epos);
			prettyString += "\n";
			spos = epos + 4;
		} // end of while

		return prettyString;

	}; // end of parseText


	function parseFiles() {

		//console.log("[parseFiles] *** starting ***");

		$scope.etree_data = [];
		var aBranchTop = {file: "", view: "", info: "", linkedSample: "", size: "", modified: "", children: []};

		aBranchTop.file = $scope.xmlFiles.Request.requestNumber;
		aBranchTop.info = $scope.xmlFiles.Request.info;

		// if FileDescriptor exists at the top level, those are files in the main directory
		if (angular.isDefined($scope.xmlFiles.Request.FileDescriptor)) {

			// make sure it's an array
			var theFileDescriptors = [];

			if (!angular.isArray($scope.xmlFiles.Request.FileDescriptor)) {
				theFileDescriptors.push($scope.xmlFiles.Request.FileDescriptor);
			}
			else {
				for (var i=0; i<$scope.xmlFiles.Request.FileDescriptor.length; i++) {
					theFileDescriptors.push($scope.xmlFiles.Request.FileDescriptor[i]);
				}
			}
			//console.log("[parseFiles] theFileDescriptors.length: " + theFileDescriptors.length);

			// add these files to the top branch
			for (var i=0;i<theFileDescriptors.length;i++) {
				var aBranchFile = {file: "", view: "", info: "", linkedSample: "", size: "", modified: "", children: []};

				aBranchFile.file = theFileDescriptors[i].displayName;
				aBranchFile.view = "";
				if (theFileDescriptors[i].viewURL != "") {
					aBranchFile.view = "/gnomexlite/" + theFileDescriptors[i].viewURL;
				}
				aBranchFile.info = "";
				aBranchFile.linkedSample = "";
				aBranchFile.size = theFileDescriptors[i].fileSizeText;
				aBranchFile.modified = theFileDescriptors[i].lastModifyDateDisplay;

				// save it
				aBranchTop.children.push(aBranchFile);
			} // end of for

		} // end of if $scope.xmlFiles.Request.FileDescriptor is defined

		if (angular.isDefined($scope.xmlFiles.Request.RequestDownload)) {

			// make sure it's an array
			var theRequestDownloads = [];

			if (!angular.isArray($scope.xmlFiles.Request.RequestDownload)) {
				theRequestDownloads.push($scope.xmlFiles.Request.RequestDownload);
			}
			else {
				for (var i=0; i<$scope.xmlFiles.Request.RequestDownload.length; i++) {
					theRequestDownloads.push($scope.xmlFiles.Request.RequestDownload[i]);
				}
			}
			//console.log("[parseFiles] theRequestDownloads.length: " + theRequestDownloads.length);

			// process each directory
			for (var i=0;i<theRequestDownloads.length;i++) {
				var aBranchDir = {file: "", view: "", info: "", linkedSample: "", size: "", modified: "", children: []};

				aBranchDir.file = theRequestDownloads[i].itemNumber;

				// are there files under the directory?
				if (angular.isDefined(theRequestDownloads[i].FileDescriptor)) {

					// make sure it's an array
					var theFileDescriptors = [];

					if (!angular.isArray(theRequestDownloads[i].FileDescriptor)) {
						theFileDescriptors.push(theRequestDownloads[i].FileDescriptor);
					}
					else {
						for (var j=0; j<theRequestDownloads[i].FileDescriptor.length; j++) {
							theFileDescriptors.push(theRequestDownloads[i].FileDescriptor[j]);
						}
					}
					//console.log("[parseFiles] RDL[" + i + "] theFileDescriptors.length: " + theFileDescriptors.length);

					// add these files to the directory branch
					for (var j=0;j<theFileDescriptors.length;j++) {
						var aBranchFile = {file: "", view: "", info: "", linkedSample: "", size: "", modified: "", children: []};

						aBranchFile.file = theFileDescriptors[j].displayName;
						aBranchFile.view = "";
						if (theFileDescriptors[j].viewURL != "") {
							aBranchFile.view = "/gnomexlite/" + theFileDescriptors[j].viewURL;
						}
						aBranchFile.info = "";
						aBranchFile.linkedSample = "";
						aBranchFile.size = theFileDescriptors[j].fileSizeText;
						aBranchFile.modified = theFileDescriptors[j].lastModifyDateDisplay;

						// save it
						aBranchDir.children.push(aBranchFile);
					} // end of for theFileDescriptors

				} // end of if theRequestDownloads[i].FileDescriptor is defined

				// save it
				aBranchTop.children.push(aBranchDir);
			} // end of for each theRequestDownloads (i.e., a directory)

		} // end of if $scope.xmlFiles.Request.RequestDownload is defined

		$scope.etree_data.push(aBranchTop);

		//console.log("[parseFiles] *** leaving *** $scope.etree_data[0].file: " + $scope.etree_data[0].file);

	}; // end of parseFiles

	 // temporary
     function mapVisibility (vis) {
		 var mapped = "";
		 if (vis == "INST") {
			 mapped = "Institution";
		 }
		 else if (vis == "MEM") {
			 mapped = "All Lab Members";
		 }
		 else if (vis == "OWNER") {
			 mapped = "Owner";
		 }
		 else if (vis == "PUBLIC") {
			 mapped = "Public";
		 }
		 return mapped;
	 }; // end of mapVisibility


	function parseExperiment() {

		//console.log("[parseExperiment] $scope.xmlExperiment.length " + $scope.xmlExperiment.length);

		// what we need for the detailInfo form
		var experSelIL = {requestor: "", name: "", submitterName: "", requestCategory: "", email: "", experimentCategoryName: "", phone: "", seqPrep: "", codeVisibility: "", organismName: "", institution: "", collaborator: "", createDate: "", lastModifyDate: "", adapter3Prime: "", adapter5Prime: ""};

		experSelIL.requestor = $scope.xmlExperiment.Request.ownerName;
		experSelIL.name = $scope.xmlExperiment.Request.name;
		experSelIL.submitterName = $scope.xmlExperiment.Request.submitterName;
		experSelIL.requestCategory = $scope.xmlExperiment.Request.requestCategory;
		experSelIL.email = $scope.xmlExperiment.Request.email;
		experSelIL.experimentCategoryName = $scope.xmlExperiment.Request.application.Application.display;
		experSelIL.phone = $scope.xmlExperiment.Request.phone;
		if ($scope.xmlExperiment.Request.seqPrepByCore == "N") {
			experSelIL.seqPrep = "Libary Prep. By Core";
		}
		else {
			experSelIL.seqPrep = "Libary Prep. By Client";
		}
		experSelIL.codeVisibility = mapVisibility($scope.xmlExperiment.Request.codeVisibility);
		experSelIL.organismName = $scope.xmlExperiment.Request.organismName;
		experSelIL.institution = "";
		experSelIL.collaborator = "";
//		if ($scope.xmlExperiment.Request.collaborators.length > 0) {
//			experSelIL.collaborator = $scope.xmlExperiment.Request.collaborators[0];
//		}
		experSelIL.createDate = $scope.xmlExperiment.Request.createDate;
		experSelIL.lastModifyDate = $scope.xmlExperiment.Request.lastModifyDate;
		experSelIL.adapter3Prime = "";
		experSelIL.adapter5Prime = "";

		// make sure protocols is an array
		var theProtocols = [];

		if (!angular.isArray($scope.xmlExperiment.Request.protocols)) {
			theProtocols.push($scope.xmlExperiment.Request.protocols);
		}
		else {
			for (var i=0; i<$scope.xmlExperiment.Request.protocols.length; i++) {
				theProtocols.push($scope.xmlExperiment.Request.protocols[i]);
			}
		}
		//console.log("[parseExperiment] theProtocols.length: " + theProtocols.length);

		// setup for easy access
		var protocolMap = new Object();
		var Adapter5Prime = "";
		var Adapter3Prime = "";

		if (theProtocols.length == 0) {
			protocolMap = null;
		}

		for (var i = 0; i < theProtocols.length; i++) {
			var aProtocol = { protocolClassName: "", name: "", label: "", Adapter5Prime: "", Adapter3Prime: ""};

			aProtocol.protocolClassName = theProtocols[i].protocolClassName;
			aProtocol.name = theProtocols[i].name;
			aProtocol.label = theProtocols[i].label;
			aProtocol.Adapter5Prime = "";
			aProtocol.Adapter3Prime = "";
			if (angular.isDefined(theProtocols[i].Adapter5Prime)) {
				aProtocol.Adapter5Prime = theProtocols[i].Adapter5Prime;
				Adapter5Prime = theProtocols[i].Adapter5Prime;
			}
			if (angular.isDefined(theProtocols[i].Adapter3Prime)) {
				aProtocol.Adapter3Prime = theProtocols[i].Adapter3Prime;
				Adapter3Prime = theProtocols[i].Adapter3Prime;
			}

			protocolMap[theProtocols[i].idProtocol] = angular.copy(aProtocol);
		} // end of for



		if (!angular.isArray($scope.xmlExperiment.Request.protocols)) {
			experSelIL.adapter3Prime = Adapter3Prime;   //$scope.xmlExperiment.Request.protocols.Protocol.Adapter3Prime;
			experSelIL.adapter5Prime = Adapter5Prime;   //$scope.xmlExperiment.Request.protocols.Protocol.Adapter5Prime;
		};
		$scope.experimentSelectedIL = angular.copy(experSelIL);

		// what we need for the description
		var experDesc = {desc: "", projdesc: "", notes: ""};

		experDesc.desc = parseText($scope.xmlExperiment.Request.description);
		experDesc.projdesc = parseText($scope.xmlExperiment.Request.projectDescription);
		experDesc.notes = parseText($scope.xmlExperiment.Request.corePrepInstructions);

		$scope.selectedDescription = angular.copy(experDesc);

		// figure out what annotations are used
		var propNameStart = "ANNOT";
		var annotList = [];
		var annotIDList = [];

		for (var propertyName in $scope.xmlExperiment.Request) {
    		var x = propertyName.substr(0, propNameStart.length);

    		if(x === propNameStart) {
				annotList.push(propertyName);
        		annotIDList.push(propertyName.substr(propNameStart.length));
    		}
		};
		//console.log("[parseExperiment] annotList.length: " + annotList.length);

		// map ANNOTNN to value for the column label
		var annotLabel = [];

		// make sure PropertyEntries is an array
		var thePropertyEntries = [];

		if (!angular.isArray($scope.xmlExperiment.Request.PropertyEntries)) {
			thePropertyEntries.push($scope.xmlExperiment.Request.PropertyEntries);
		}
		else {
			for (var i=0; i<$scope.xmlExperiment.Request.PropertyEntries.length; i++) {
				thePropertyEntries.push($scope.xmlExperiment.Request.PropertyEntries[i]);
			}
		}
		//console.log("[parseExperiment] thePropertyEntries.length: " + thePropertyEntries.length);

		// got to be a better way.... good thing the lists are short
		for (var i = 0; i < annotIDList.length; i++) {
			for (var j = 0; j <thePropertyEntries.length; j++) {
				if (annotIDList[i] === thePropertyEntries[j].idProperty) {
					annotLabel.push(thePropertyEntries[j].name);
					break;
				}
			}; // end of thePropertyEntries for
		}; // end of annotIDList for
		//console.log("[parseExperiment] annotLabel.length: " + annotLabel.length);


		// make sure samples is an array
		var theSamples = [];

		// ** NOTE ** if there is only 1 sample sometimes it looks like "samples": {"Sample": {
	    // get rid of the extra Sample level
		if (!angular.isArray($scope.xmlExperiment.Request.samples)) {
			if (angular.isDefined($scope.xmlExperiment.Request.samples.Sample)) {
				theSamples.push($scope.xmlExperiment.Request.samples.Sample)
			}
			else {
				theSamples.push($scope.xmlExperiment.Request.samples);
			}
		}
		else {
			for (var i=0; i<$scope.xmlExperiment.Request.samples.length; i++) {
				theSamples.push($scope.xmlExperiment.Request.samples[i]);
			}
		}
		//console.log("[parseExperiment] theSamples.length: " + theSamples.length);


		// make sure multiplexSequenceLanes is an array
		var themultiplexSequenceLanes = [];

		if (!angular.isArray($scope.xmlExperiment.Request.multiplexSequenceLanes)) {
			themultiplexSequenceLanes.push($scope.xmlExperiment.Request.multiplexSequenceLanes);
		}
		else {
			for (var i=0; i<$scope.xmlExperiment.Request.multiplexSequenceLanes.length; i++) {
				themultiplexSequenceLanes.push($scope.xmlExperiment.Request.multiplexSequenceLanes[i]);
			}
		}
		//console.log("[parseExperiment] themultiplexSequenceLanes.length: " + themultiplexSequenceLanes.length);

		// are we HISEQ and not external?
		if ($scope.xmlExperiment.Request.isExternal != "Y" && $scope.xmlExperiment.Request.codeRequestCategory == "HISEQ") {

		// build the list of column labels for the Experiment Design table
		var sampleLabels = [];

		sampleLabels.push("Multiplex Group #");
		sampleLabels.push("ID");
		sampleLabels.push("Sample Name");
		sampleLabels.push("Conc.");

		for (var i=0; i<annotLabel.length; i++) {
			sampleLabels.push(annotLabel[i]);
		};

		sampleLabels.push("Sample Type");
		sampleLabels.push("Organism");
		sampleLabels.push("Seq Lib Protocol");

		sampleLabels.push("Index Tag A");
		sampleLabels.push("Nucl Acid Extraction");
		sampleLabels.push("QC Conc");
		sampleLabels.push("Ave Insert Size");
		sampleLabels.push("QC RIN");
		sampleLabels.push("QC Status");
		sampleLabels.push("Seq Lib Prep Status");
		sampleLabels.push("Core to Prep?");
		sampleLabels.push("Seq Lib Conc");

		// remember column headers for the table
		$scope.selectedExprDesignLabelsIL = angular.copy(sampleLabels);

		$scope.selectedExprDesignIL = [];
		// generate the data for the selectedSamples table
		for (var i = 0; i < theSamples.length; i++) {
			var aSample = new Object();

			aSample[sampleLabels[0]] = theSamples[i].multiplexGroupNumber;

			aSample[sampleLabels[1]] = theSamples[i].number;
			aSample[sampleLabels[2]] = theSamples[i].name;
			aSample[sampleLabels[3]] = theSamples[i].concentration;

			// the rest
			for (var j = 0; j < annotList.length; j++) {
				aSample[sampleLabels[j+4]] = theSamples[i][annotList[j]];

				// map the code to the text if we can
				if (angular.isDefined($rootScope.propertyOptions[annotIDList[j]]) ) {
					var valueMap = angular.copy($rootScope.propertyOptions[annotIDList[j]]);
					if (angular.isDefined(valueMap[ theSamples[i][annotList[j]] ]) ) {
						aSample[sampleLabels[j+4]] = valueMap[ theSamples[i][annotList[j]] ];
					}
				}
			};

			var ipos = annotList.length + 4;
			aSample[sampleLabels[ipos]] = "";
			if (angular.isDefined(theSamples[i].sampleType) ) {
				aSample[sampleLabels[ipos]] = theSamples[i].sampleType;
			}
			else {
				if (angular.isDefined(theSamples[i].idSampleType) ) {
					aSample[sampleLabels[ipos]] = theSamples[i].idSampleType;
				}
			}

			aSample[sampleLabels[ipos+1]] = $scope.xmlExperiment.Request.organismName;
			aSample[sampleLabels[ipos+2]] = "";
			if (protocolMap != null && angular.isDefined (protocolMap[theSamples[i].idSeqLibProtocol]) ) {
				aSample[sampleLabels[ipos+2]] = protocolMap[theSamples[i].idSeqLibProtocol].name;
			}
			aSample[sampleLabels[ipos+3]] = "";					// haven't figured out where to get Index Tag A
			aSample[sampleLabels[ipos+4]] = theSamples[i].otherSamplePrepMethod;
			aSample[sampleLabels[ipos+5]] = theSamples[i].qualCalcConcentration;
			aSample[sampleLabels[ipos+6]] = theSamples[i].meanLibSizeActual;
			aSample[sampleLabels[ipos+7]] = theSamples[i].qualRINNumber;
			aSample[sampleLabels[ipos+8]] = theSamples[i].qualStatus;
			aSample[sampleLabels[ipos+9]] = theSamples[i].seqPrepStatus;
			aSample[sampleLabels[ipos+10]] = theSamples[i].seqPrepByCore;
			aSample[sampleLabels[ipos+11]] = theSamples[i].seqPrepLibConcentration;


			// add it
			$scope.selectedExprDesignIL.push(aSample);
		}; // end of theSamples.length for

		// *** done with samples, now do sequenceLanes ***

		// make sure sequenceLanes is an array
		var theSequenceLanes = [];

		if (!angular.isArray($scope.xmlExperiment.Request.sequenceLanes)) {
			theSequenceLanes.push($scope.xmlExperiment.Request.sequenceLanes);
		}
		else {
			for (var i=0; i<$scope.xmlExperiment.Request.sequenceLanes.length; i++) {
				theSequenceLanes.push($scope.xmlExperiment.Request.sequenceLanes[i]);
			}
		}
		//console.log("[parseExperiment] theSequenceLanes.length: " + theSequenceLanes.length);

		// build the list of column labels for the Sequence Lanes table
		var sequenceLabels = [];

		sequenceLabels.push("Flow Cell #");			//0
		sequenceLabels.push("Channel");				//1

		sequenceLabels.push("ID");					//2
		sequenceLabels.push("Sample Name");			//3
		sequenceLabels.push("Sample ID");			//4

		sequenceLabels.push("Illumina Barcode");	//5
		sequenceLabels.push("Illumina Barcode B");	//6
		sequenceLabels.push("Custom Barcode");		//7

		sequenceLabels.push("Custom Barcode B");	//8
		sequenceLabels.push("Sequencing Protocol");	//9
		sequenceLabels.push("Status in Workflow");	//10
		sequenceLabels.push("Last Cycle Status");	//11

		// remember column headers for the table
		$scope.selectedSequenceLabelsIL = angular.copy(sequenceLabels);

		$scope.selectedSequencesIL = [];

		// generate the data for the sequence lanes table
		for (var i = 0; i < theSequenceLanes.length; i++) {
			var aSequence = new Object();

			aSequence[sequenceLabels[0]] = theSequenceLanes[i].flowCellNumber;
			aSequence[sequenceLabels[1]] = theSequenceLanes[i].flowCellChannelNumber;
			aSequence[sequenceLabels[2]] = theSequenceLanes[i].number;
			aSequence[sequenceLabels[3]] = theSequenceLanes[i].sampleName;
			aSequence[sequenceLabels[4]] = theSequenceLanes[i].sampleNumber;

			aSequence[sequenceLabels[5]] = theSequenceLanes[i].sampleBarcodeSequence;
			aSequence[sequenceLabels[6]] = theSequenceLanes[i].sampleBarcodeSequenceB;
			aSequence[sequenceLabels[7]] = theSequenceLanes[i].sampleBarcodeSequence;

			aSequence[sequenceLabels[8]] = ""; // haven't figured out where to get Custom Barcode B
			aSequence[sequenceLabels[9]] = "";
			if (protocolMap != null && angular.isDefined(protocolMap[theSequenceLanes[i].idNumberSequencingCyclesAllowed]) ) {
				aSequence[sequenceLabels[9]] = protocolMap[theSequenceLanes[i].idNumberSequencingCyclesAllowed].name;
			}
			aSequence[sequenceLabels[10]] = theSequenceLanes[i].workflowStatus;
			aSequence[sequenceLabels[11]] = theSequenceLanes[i].lastCycleStatus;

			// add it
			$scope.selectedSequencesIL.push(aSequence);
		}; // end of theSequences.length for
	} // end of if HISEQ and not external

		// deal with external experiment and HISEQ
		if ($scope.xmlExperiment.Request.isExternal != "N") {

		// build the list of column labels for the grid
		var hasMGN = false;
		if (theSamples.length > 0) {
			if (angular.isDefined(theSamples[0].multiplexGroupNumber) && theSamples[0].multiplexGroupNumber.length > 0) {
				hasMGN = true;
			}
		}

		// if the samples have a description, show it
		var hasDescription = false;
		if (angular.isDefined($scope.xmlExperiment.Request.hasSampleDescription)) {
			if ($scope.xmlExperiment.Request.hasSampleDescription == "Y") {
				hasDescription = true;
			}
		}

		var sampleLabels = [];

		if (hasMGN) {
			sampleLabels.push("Multiplex Group #");
		}
		sampleLabels.push("ID");
		sampleLabels.push("Sample Name");

		if (hasMGN) {
			sampleLabels.push("Index Tag A");
		}
		sampleLabels.push("Sample Type");

		for (var i=0; i<annotLabel.length; i++) {
			sampleLabels.push(annotLabel[i]);
		};

		if (hasDescription) {
			sampleLabels.push("Description");
		}

		// remember column headers for the table
		$scope.selectedSampleLabelsIL = angular.copy(sampleLabels);

		$scope.selectedSamplesIL = [];
		// generate the data for the selectedSamples table
		var ipos = 0;
		for (var i = 0; i < theSamples.length; i++) {
			var aSample = new Object();

			ipos = 0;
			if (hasMGN) {
				aSample[sampleLabels[ipos]] = theSamples[i].multiplexGroupNumber;
				ipos = 1;
			}

			aSample[sampleLabels[ipos]] = theSamples[i].number;
			ipos += 1;
			aSample[sampleLabels[ipos]] = theSamples[i].name;
			ipos += 1;
			if (hasMGN) {
				aSample[sampleLabels[ipos]] = "";					// haven't figured out where to get Index Tag A
				ipos += 1;
			}

			aSample[sampleLabels[ipos]] = "";
			if (angular.isDefined(theSamples[i].sampleType) ) {
				aSample[sampleLabels[ipos]] = theSamples[i].sampleType;
			}
			else {
				if (angular.isDefined(theSamples[i].idSampleType) ) {
					aSample[sampleLabels[ipos]] = theSamples[i].idSampleType;
				}
			}
			ipos += 1;

			// the rest
			for (var j = 0; j < annotList.length; j++) {
				aSample[sampleLabels[j+ipos]] = theSamples[i][annotList[j]];

				// map the code to the text if we can
				if (angular.isDefined($rootScope.propertyOptions[annotIDList[j]]) ) {
					var valueMap = angular.copy($rootScope.propertyOptions[annotIDList[j]]);
					if (angular.isDefined(valueMap[ theSamples[i][annotList[j]] ]) ) {
						aSample[sampleLabels[j+ipos]] = valueMap[ theSamples[i][annotList[j]] ];
					}
				}
			};

			if (hasDescription) {
				ipos = ipos + annotList.length;
				aSample[sampleLabels[ipos]] = parseText(theSamples[i].description);
			}

			// add it
			$scope.selectedSamplesIL.push(aSample);
		}; // end of theSamples.length for

		} // end of if HISEQ and external experiment

	}; // end of parseExperiment


     function buildExperimentTree () {

		//console.log("[buildExperimentTree] $scope.projectRequestList.length " + $scope.projectRequestList.length);
		$scope.experimentTree = [];

		// make an array of labs
		var theLabs = [];

		if (!angular.isArray($scope.projectRequestList.Lab)) {
			theLabs.push($scope.projectRequestList.Lab);
		}
		else {
			for (var i=0; i<$scope.projectRequestList.Lab.length; i++) {
				theLabs.push($scope.projectRequestList.Lab[i]);
			}
		}
		//console.log("[buildExperimentTree] theLabs.length: " + theLabs.length);

		// process the labs
		for (var i = 0; i < theLabs.length; i++) {
			//console.log("Processing Lab[" + i + "]: " + theLabs[i].label);

			var theLab = {type: "L", label: "", children: []};
			theLab.label = theLabs[i].label;

			// make an array out of Projects
			var theProjects = [];

			if (!angular.isArray(theLabs[i].Project)) {
				theProjects.push(theLabs[i].Project);
			}
			else {
				for (var j=0; j<theLabs[i].Project.length; j++) {
					theProjects.push(theLabs[i].Project[j]);
				}
			}
			//console.log("[buildExperimentTree] Lab[" + i + "] theProjects.length: " + theProjects.length);

			// process the projects
			for (var j=0; j<theProjects.length; j++) {
				//console.log("[buildExperimentTree] Processing Project[" + j + "]: " + theProjects[j].label);

				var theProject = {type: "P", label: "", children: []};
				theProject.label = theProjects[j].label;

				// make an array out of RequestCategory
				var theRequestCategorys = [];

				if (!angular.isArray(theProjects[j].RequestCategory)) {
					theRequestCategorys.push(theProjects[j].RequestCategory);
				}
				else {
					for (var k=0; k<theProjects[j].RequestCategory.length; k++) {
						theRequestCategorys.push(theProjects[j].RequestCategory[k]);
					}
				}
				//console.log("[buildExperimentTree] Project[" + j + "] theRequestCategorys.length: " + theRequestCategorys.length);

				// process the requestcategorys
				for (var k=0; k<theRequestCategorys.length; k++) {
					//console.log("[buildExperimentTree] Processing RequestCategory[" + k + "]: " + theRequestCategorys[k].idProject);

					// make an array out of Requests
					var theRequests = [];

					if (!angular.isArray(theRequestCategorys[k].Request)) {
						theRequests.push(theRequestCategorys[k].Request);
					}
					else {
						for (var l=0; l<theRequestCategorys[k].Request.length; l++) {
							theRequests.push(theRequestCategorys[k].Request[l]);
						}
					}
					//console.log("[buildExperimentTree] Project[" + j + "] theRequests.length: " + theRequests.length);

					// process the requests
					for (var l=0; l<theRequests.length; l++) {
						//console.log("[buildExperimentTree] Processing Request[" + l + "]: " + theRequests[l].requestNumber);

						var theExperiment = {type: "E", label: "", etype: "", number: "", date: "", requestor: "", project: "", kind: "", microarray: "", analysis: " ", analysisnames: " ", children: [] };
						theExperiment.label = theRequests[l].requestNumber + " - " + theRequests[l].name;

						// shorten it
						if (theExperiment.label.length > 31) {
							theExperiment.label = theExperiment.label.substr(0, 31);
						}


						// set up experiment type
						if (theRequests[l].codeRequestCategory === "HISEQ" && theRequests[l].isExternal === "N") {
							theExperiment.etype = "IL";
						}

						if (theRequests[l].isExternal === "Y" || theRequests[l].codeRequestCategory != "HISEQ") {
							theExperiment.etype = "EIL";
						}

						theExperiment.number = theRequests[l].requestNumber;
						theExperiment.date = theRequests[l].createDate;
						theExperiment.requestor = theRequests[l].ownerLastName + ", " + theRequests[l].ownerFirstName;
						theExperiment.project = theRequests[l].projectName;
						theExperiment.kind = theRequests[l].codeApplication;    //codeRequestCategory;			// this is WRONG
						theExperiment.microarray = " ";
						theExperiment.analysis = " ";
						theExperiment.analysisnames = theRequests[l].analysisNames;

						theProject.children.push(theExperiment);
					} // end of theRequests for

				} // end of theRequestCategorys for

				theLab.children.push(theProject);
			} // end of theProjects for

			$scope.experimentTree.push(theLab);
		} // end of theLabs for

		// make the tree be expanded by default
		for (var i = 0; i<$scope.experimentTree.length; i++) {
			$scope.expandedExperimentTree.push($scope.experimentTree[i]);

			for (var j = 0;j<$scope.experimentTree[i].children.length;j++) {
				$scope.expandedExperimentTree.push($scope.experimentTree[i].children[j]);
			}
		}

	  // automatically setup the initial experiment list for the first lab
	  if ($scope.showColumnForm) {
	     var sel = angular.copy ($scope.experimentTree[0]);
         $scope.selectedNode = sel;
         if (sel.type == "L") {
		 	$scope.labSelected = sel.label;
		 	$scope.headingSelected = $scope.labSelected;
		 	$scope.experiments = [];

		 	$scope.projects = angular.copy(sel.children);

			// Go through each project and collect all the experiments
			//console.log("$scope.projects.length " + $scope.projects.length);
			for (var i=0; i<$scope.projects.length; i++) {
				//console.log("Processing project: " + $scope.projects[i].label);
				for (var j=0; j<$scope.projects[i].children.length; j++) {
					//console.log("Processing child: " + $scope.projects[i].children[j].label);
					$scope.experiments.push($scope.projects[i].children[j]);
				}
			}

		 	$scope.numberOfExperiments = $scope.experiments.length;
		 	$scope.experimentForm = $scope.experimentList;
		}
	} else {
		// we got here doing a Lookup, pretend someone clicked on the experiment of interest...
	     var sel = angular.copy ($scope.experimentTree[0]);
         $scope.selectedNode = sel;
         if (sel.type == "L") {
		 	$scope.labSelected = sel.label;
		 	$scope.headingSelected = $scope.labSelected;
		 	$scope.experiments = [];

		 	$scope.projects = angular.copy(sel.children);

			// Go through each project and collect all the experiments
			var foundIT = false;
			//console.log("$scope.projects.length " + $scope.projects.length);
			for (var i=0; i<$scope.projects.length; i++) {
				//console.log("Processing project: " + $scope.projects[i].label);
				for (var j=0; j<$scope.projects[i].children.length; j++) {
					//console.log("Processing child: " + $scope.projects[i].children[j].label);

					// is it the one we want?
					//console.log("[bET lookup] " + $scope.projects[i].children[j].number + " " + $scope.searchFilter.lookup);
					if ($scope.projects[i].children[j].number == $scope.searchFilter.lookup) {
						// yes, set things up
						var sel = angular.copy($scope.projects[i].children[j]);
						$scope.experimentSelected = angular.copy($scope.projects[i].children[j]);
						$scope.experiments = angular.copy($scope.projects[i].children[j]);
						$scope.numberOfExperiments = $scope.experiments.length;
						if (sel.etype == "IL") {
						$scope.experimentForm = $scope.experimentDetailIL;
						}
						if (sel.etype == "EIL") {
						$scope.experimentForm = $scope.experimentDetailEIL;
						}
						if (sel.etype == "HS") {
						$scope.experimentForm = $scope.experimentDetailHS;
						}
						if (sel.etype == "DNA") {
						$scope.experimentForm = $scope.experimentDetailDNA;
						}

						foundIT = true;
						break;
					} // end of if
				} // end of for j
				if (foundIT) {
					break;
				}
			} // end of for i

		} // end of if sel.type == "L"

	} // end of else

	 }; // end of buildExperimentTree




$scope.mymodal = null;

    $scope.openDownloadWindow = function(e) {
    	$scope.mymodal = $modal.open({
    		templateUrl: 'app/experiment/experimentDownload.html',
    		controller: 'ExperimentController'
    	});

    	$scope.mymodal.result.then(function (dlist) {
	    });
    };

	$scope.ok = function () {
	  $scope.mymodal.close($scope.downloadList);
	};

	$scope.cancel = function () {
	  $scope.mymodal.dismiss('cancel');
	};

}]);


