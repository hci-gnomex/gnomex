'use strict';

/**
 * AnalysisController
 * @constructor
 */

var analysis    = angular.module('analysis', ['ui.bootstrap','treeControl','treeGrid', 'filters', 'services', 'directives','chosen','ngProgress','dialogs.main','error']);
console.log("In AnalysisController.js");

angular.module("analysis")
  .factory('AnalysisGroupListService', function($http) {
    return {
      getAnalyses: function(whatToGet) {
         return $http.get('/gnomexlite/GetAnalysisGroupList.gx' + whatToGet);
      }
    }
  })
  .factory('AnalysisGroupService', function($http) {
    return {
      getAnalysisGroup: function(analgroupid) {
         return $http.get('/gnomexlite/GetAnalysisGroup.gx?idAnalysisGroup=' + analgroupid);
      }
    }
  })
  .factory('AnalysisService', function($http) {
    return {
      getAnalysis: function(analid) {
         return $http.get('/gnomexlite/GetAnalysis.gx?idAnalysis=' + analid);
      }
    }
  })
  .factory('AnalysisDownloadListService', function($http) {
    return {
      getAnalysisDownloadList: function(exprid) {
         return $http.get('/gnomexlite/GetAnalysisDownloadList.gx?idAnalysis=' + exprid);
      }
    }
  })

.controller("AnalysisController", [
'$scope', '$http', '$modal','$rootScope','$q','ngProgress','dialogs','AnalysisGroupService','AnalysisGroupListService','AnalysisService','AnalysisDownloadListService',
function($scope, $http, $modal, $rootScope, $q, ngProgress, dialogs, AnalysisGroupService, AnalysisGroupListService, AnalysisService, AnalysisDownloadListService) {
	/**********************
	 * Initialization!
	 *********************/
console.log("In AnalysisController.js 2");
$scope.xmlAnalysis = "";
$scope.analysisRequestList = "";
$scope.okToFind = true;						// comes from $rootScope
$scope.okToPickLab = true;					// comes from $rootScope
$scope.whatToLookup = $rootScope.whatAnalysisToLookup;
$scope.showColumnForm = true;

$scope.searchFilter = {selectAll: false, group: "", owner: "", lookup: ""};

$scope.labs = [];							// comes from $rootScope
$scope.analysisTree = [];
$scope.expandedAnalysisTree = [];

$scope.selectedDescription = {};

$scope.selectedSampleLabelsIL = [];
$scope.selectedSamplesIL = [];

$scope.selectedExperDesignLabelsIL = [];
$scope.selectedExperDesignIL = [];

$scope.selectedSequenceLabelsIL = [];
$scope.selectedSequencesIL = [];

$scope.xmlFiles = "";
$scope.atree_data = [];

$scope.aexpanding_property = {field: "file",displayName: "File or Folder Name"};
$scope.acol_defs = [
	{field: "view", displayName: "View", cellTemplate: '<div ng-show="{{ row.branch[col.field] }}"><a ng-href="{{ row.branch[col.field] }}">view</a></div>'},
	{field: "comment", displayName: "Comment"},
	{field: "size", displayName: "Size"},
	{field: "modified", displayName: "Modified"}];

// fake data
//$scope.reqDownloadList = {"Analysis":{"description":"","idCoreFacility":"","createDate":"2013-01-16","idSubmitter":"47","canRead":"Y","canUpdate":"Y","canDelete":"Y","canUpdateVisibility":"Y","canUploadData":"Y","ownerName":"Jeffrey Alexander","submitterName":"Jeffrey Alexander","labName":"Bruneau, Benoit Lab","createYear":"2013","idAnalysis":"108","analysisGroupNames":"Alexander_Misc","name":"HDACs_Brg1","key":"2013-20130116-A108","number":"A108","displayName":"HDACs_Brg1","idLab":"13","isSelected":"N","state":"unchecked","isEmpty":"N","AnalysisFileDescriptor":[{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5187_mm9_bestmap.bw","fileSize":"28305208","fileSizeText":"27  mb","childFileSize":"28305208","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5187_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 16 22:47:10 MST 2013","zipEntryName":"A108/121219BruA_D12-5187_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14639","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5188_mm9_bestmap.bw","fileSize":"70740865","fileSizeText":"67  mb","childFileSize":"70740865","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5188_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 16 22:42:57 MST 2013","zipEntryName":"A108/121219BruA_D12-5188_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14638","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5189_mm9_bestmap.bw","fileSize":"58272976","fileSizeText":"56  mb","childFileSize":"58272976","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5189_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 16 22:32:28 MST 2013","zipEntryName":"A108/121219BruA_D12-5189_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14637","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5189_mm9_bestmap.sam.NORAND.mapped.bam.sorted.uniq.bb","fileSize":"40026471","fileSizeText":"38  mb","childFileSize":"40026471","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5189_mm9_bestmap.sam.NORAND.mapped.bam.sorted.uniq.bb","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Thu Jan 24 10:39:55 MST 2013","zipEntryName":"A108/121219BruA_D12-5189_mm9_bestmap.sam.NORAND.mapped.bam.sorted.uniq.bb","number":"A108","idAnalysisFileString":"14657","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5189_mm9_bestmap.sam.NORAND.mapped.bam.sorted.uniq.useq","fileSize":"9506213","fileSizeText":"9  mb","childFileSize":"9506213","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5189_mm9_bestmap.sam.NORAND.mapped.bam.sorted.uniq.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Thu Jan 24 10:35:43 MST 2013","zipEntryName":"A108/121219BruA_D12-5189_mm9_bestmap.sam.NORAND.mapped.bam.sorted.uniq.useq","number":"A108","idAnalysisFileString":"14656","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5190_mm9_bestmap.bw","fileSize":"253003361","fileSizeText":"241  mb","childFileSize":"253003361","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5190_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Thu Jan 17 00:30:09 MST 2013","zipEntryName":"A108/121219BruA_D12-5190_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14641","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5191_mm9_bestmap.bw","fileSize":"226805236","fileSizeText":"216  mb","childFileSize":"226805236","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5191_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 16 23:54:12 MST 2013","zipEntryName":"A108/121219BruA_D12-5191_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14640","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5192_mm9_bestmap.bw","fileSize":"383559550","fileSizeText":"366  mb","childFileSize":"383559550","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5192_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 23 17:30:39 MST 2013","zipEntryName":"A108/121219BruA_D12-5192_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14650","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5193_mm9_bestmap.bw","fileSize":"465499244","fileSizeText":"444  mb","childFileSize":"465499244","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5193_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 23 17:33:09 MST 2013","zipEntryName":"A108/121219BruA_D12-5193_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14651","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5193_mm9_bestmap.sam.NORAND.mapped.bam.sorted.bw","fileSize":"464824581","fileSizeText":"443  mb","childFileSize":"464824581","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5193_mm9_bestmap.sam.NORAND.mapped.bam.sorted.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Thu Jan 24 16:32:39 MST 2013","zipEntryName":"A108/121219BruA_D12-5193_mm9_bestmap.sam.NORAND.mapped.bam.sorted.bw","number":"A108","idAnalysisFileString":"14658","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5194_mm9_bestmap.bw","fileSize":"344","fileSizeText":"344 b","childFileSize":"344","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5194_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 23 17:33:10 MST 2013","zipEntryName":"A108/121219BruA_D12-5194_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14652","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5195_mm9_bestmap.bw","fileSize":"265523623","fileSizeText":"253  mb","childFileSize":"265523623","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5195_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 23 17:34:35 MST 2013","zipEntryName":"A108/121219BruA_D12-5195_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14653","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5196_mm9_bestmap.bw","fileSize":"295077102","fileSizeText":"281  mb","childFileSize":"295077102","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5196_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 23 17:35:59 MST 2013","zipEntryName":"A108/121219BruA_D12-5196_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14654","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"121219BruA_D12-5197_mm9_bestmap.bw","fileSize":"344","fileSizeText":"344 b","childFileSize":"344","fileName":"/Data01/gnomex/AnalysisData/2013/A108/121219BruA_D12-5197_mm9_bestmap.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 23 17:36:00 MST 2013","zipEntryName":"A108/121219BruA_D12-5197_mm9_bestmap.bw","number":"A108","idAnalysisFileString":"14655","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"Flag_Brg1_D4_ChIPseq.bw","fileSize":"69916501","fileSizeText":"67  mb","childFileSize":"69916501","fileName":"/Data01/gnomex/AnalysisData/2013/A108/Flag_Brg1_D4_ChIPseq.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Mon Feb 04 14:59:05 MST 2013","zipEntryName":"A108/Flag_Brg1_D4_ChIPseq.bw","number":"A108","idAnalysisFileString":"14761","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"Flag_Brg1_D4_ChIPseq.useq","fileSize":"15234195","fileSizeText":"15  mb","childFileSize":"15234195","fileName":"/Data01/gnomex/AnalysisData/2013/A108/Flag_Brg1_D4_ChIPseq.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:36 MST 2013","zipEntryName":"A108/Flag_Brg1_D4_ChIPseq.useq","number":"A108","idAnalysisFileString":"14660","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"Flag_Brg1_D4_ChIPseq.wig.bw","fileSize":"86084316","fileSizeText":"82  mb","childFileSize":"86084316","fileName":"/Data01/gnomex/AnalysisData/2013/A108/Flag_Brg1_D4_ChIPseq.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:19:41 MST 2013","zipEntryName":"A108/Flag_Brg1_D4_ChIPseq.wig.bw","number":"A108","idAnalysisFileString":"14679","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"Flag_Brg1_D4_ChIPseq_background.bw","fileSize":"96138887","fileSizeText":"92  mb","childFileSize":"96138887","fileName":"/Data01/gnomex/AnalysisData/2013/A108/Flag_Brg1_D4_ChIPseq_background.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Mon Feb 04 15:00:08 MST 2013","zipEntryName":"A108/Flag_Brg1_D4_ChIPseq_background.bw","number":"A108","idAnalysisFileString":"14760","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"Flag_Brg1_D4_ChIPseq_background.useq","fileSize":"16504343","fileSizeText":"16  mb","childFileSize":"16504343","fileName":"/Data01/gnomex/AnalysisData/2013/A108/Flag_Brg1_D4_ChIPseq_background.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:32 MST 2013","zipEntryName":"A108/Flag_Brg1_D4_ChIPseq_background.useq","number":"A108","idAnalysisFileString":"14659","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"Flag_Brg1_D4_ChIPseq_background.wig.bw","fileSize":"110088577","fileSizeText":"105  mb","childFileSize":"110088577","fileName":"/Data01/gnomex/AnalysisData/2013/A108/Flag_Brg1_D4_ChIPseq_background.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:18:57 MST 2013","zipEntryName":"A108/Flag_Brg1_D4_ChIPseq_background.wig.bw","number":"A108","idAnalysisFileString":"14678","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep1.bw","fileSize":"42971550","fileSizeText":"41  mb","childFileSize":"42971550","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep1.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:33:34 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep1.bw","number":"A108","idAnalysisFileString":"14672","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep1.useq","fileSize":"10398934","fileSizeText":"10  mb","childFileSize":"10398934","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep1.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:44 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep1.useq","number":"A108","idAnalysisFileString":"14662","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep1.wig.bw","fileSize":"58327112","fileSizeText":"56  mb","childFileSize":"58327112","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep1.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:20:53 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep1.wig.bw","number":"A108","idAnalysisFileString":"14681","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep1_background.bw","fileSize":"78101524","fileSizeText":"74  mb","childFileSize":"78101524","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep1_background.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:32:24 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep1_background.bw","number":"A108","idAnalysisFileString":"14671","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep1_background.useq","fileSize":"14505820","fileSizeText":"14  mb","childFileSize":"14505820","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep1_background.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:40 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep1_background.useq","number":"A108","idAnalysisFileString":"14661","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep1_background.wig.bw","fileSize":"82656118","fileSizeText":"79  mb","childFileSize":"82656118","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep1_background.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:20:23 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep1_background.wig.bw","number":"A108","idAnalysisFileString":"14680","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep2.bw","fileSize":"40946897","fileSizeText":"39  mb","childFileSize":"40946897","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep2.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:34:17 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep2.bw","number":"A108","idAnalysisFileString":"14673","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep2.useq","fileSize":"10232484","fileSizeText":"10  mb","childFileSize":"10232484","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep2.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:47 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep2.useq","number":"A108","idAnalysisFileString":"14663","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_4OHT_Rep2.wig.bw","fileSize":"56231848","fileSizeText":"54  mb","childFileSize":"56231848","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_4OHT_Rep2.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:21:21 MST 2013","zipEntryName":"A108/H3K27ac_D4_4OHT_Rep2.wig.bw","number":"A108","idAnalysisFileString":"14682","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1.bw","fileSize":"42439622","fileSizeText":"40  mb","childFileSize":"42439622","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:28:36 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1.bw","number":"A108","idAnalysisFileString":"14669","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1.useq","fileSize":"10541655","fileSizeText":"10  mb","childFileSize":"10541655","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 15:06:34 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1.useq","number":"A108","idAnalysisFileString":"14674","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1.wig.bw","fileSize":"57969323","fileSizeText":"55  mb","childFileSize":"57969323","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:18:01 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1.wig.bw","number":"A108","idAnalysisFileString":"14677","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1_background.bw","fileSize":"101734323","fileSizeText":"97  mb","childFileSize":"101734323","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1_background.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:23:59 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1_background.bw","number":"A108","idAnalysisFileString":"14668","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1_background.useq","fileSize":"16875993","fileSizeText":"16  mb","childFileSize":"16875993","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1_background.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:52 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1_background.useq","number":"A108","idAnalysisFileString":"14664","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1_background.wig.bw","fileSize":"106428922","fileSizeText":"101  mb","childFileSize":"106428922","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1_background.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:17:32 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1_background.wig.bw","number":"A108","idAnalysisFileString":"14676","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep1_fixed.bw","fileSize":"57969323","fileSizeText":"55  mb","childFileSize":"57969323","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep1_fixed.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 15:55:07 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep1_fixed.bw","number":"A108","idAnalysisFileString":"14675","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep2.bw","fileSize":"54108823","fileSizeText":"52  mb","childFileSize":"54108823","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep2.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:30:23 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep2.bw","number":"A108","idAnalysisFileString":"14670","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep2.useq","fileSize":"11598080","fileSizeText":"11  mb","childFileSize":"11598080","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep2.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:19:58 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep2.useq","number":"A108","idAnalysisFileString":"14666","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"H3K27ac_D4_THF_Rep2.wig.bw","fileSize":"67905870","fileSizeText":"65  mb","childFileSize":"67905870","fileName":"/Data01/gnomex/AnalysisData/2013/A108/H3K27ac_D4_THF_Rep2.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:21:56 MST 2013","zipEntryName":"A108/H3K27ac_D4_THF_Rep2.wig.bw","number":"A108","idAnalysisFileString":"14683","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"HDAC1_ESC_ChIPseq.wig.bw","fileSize":"48597222","fileSizeText":"46  mb","childFileSize":"48597222","fileName":"/Data01/gnomex/AnalysisData/2013/A108/HDAC1_ESC_ChIPseq.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 30 15:25:37 MST 2013","zipEntryName":"A108/HDAC1_ESC_ChIPseq.wig.bw","number":"A108","idAnalysisFileString":"14698","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"HDAC2_ESC_ChIPseq.wig.bw","fileSize":"108005501","fileSizeText":"103  mb","childFileSize":"108005501","fileName":"/Data01/gnomex/AnalysisData/2013/A108/HDAC2_ESC_ChIPseq.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Wed Jan 30 15:26:07 MST 2013","zipEntryName":"A108/HDAC2_ESC_ChIPseq.wig.bw","number":"A108","idAnalysisFileString":"14699","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"HDAC3_ESC_ChIPseq.bw","fileSize":"73291560","fileSizeText":"70  mb","childFileSize":"73291560","fileName":"/Data01/gnomex/AnalysisData/2013/A108/HDAC3_ESC_ChIPseq.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Mon Feb 04 15:00:53 MST 2013","zipEntryName":"A108/HDAC3_ESC_ChIPseq.bw","number":"A108","idAnalysisFileString":"14759","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"HDAC3_ESC_ChIPseq.useq","fileSize":"11001068","fileSizeText":"10  mb","childFileSize":"11001068","fileName":"/Data01/gnomex/AnalysisData/2013/A108/HDAC3_ESC_ChIPseq.useq","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 13:20:01 MST 2013","zipEntryName":"A108/HDAC3_ESC_ChIPseq.useq","number":"A108","idAnalysisFileString":"14667","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"},{"idAnalysis":"108","dirty":"N","key":"2013-20130116-A108","displayName":"HDAC3_ESC_ChIPseq.wig.bw","fileSize":"84085832","fileSizeText":"80  mb","childFileSize":"84085832","fileName":"/Data01/gnomex/AnalysisData/2013/A108/HDAC3_ESC_ChIPseq.wig.bw","qualifiedFilePath":"","baseFilePath":"/Data01/gnomex/AnalysisData/2013/A108","comments":"","lastModifyDate":"Fri Jan 25 16:22:38 MST 2013","zipEntryName":"A108/HDAC3_ESC_ChIPseq.wig.bw","number":"A108","idAnalysisFileString":"14684","idLab":"13","isSelected":"N","state":"unchecked","isSupportedDataTrack":"Y","viewURL":"","hasDataTrack":"Y"}]}};
$scope.analysisType = [{atype: "1", type: "ChIP-Seq analysis"},
{atype: "2", type: "TiMAT2 Tiling Analysis"},
{atype: "3", type: "RNA-Seq, Differential"},
{atype: "4", type: "Viral sequence screen"},
{atype: "5", type: "small RNA-Seq"},
{atype: "6", type: "aCGH"},
{atype: "7", type: "Digital Gene Expression (DiGE)"},
{atype: "8", type: "Microarray Design"},
{atype: "9", type: "Alignment"},
{atype: "10", type: "Cap-Seq"},
{atype: "11", type: "SNP/INDEL"},
{atype: "12", type: "High level analysis"},
{atype: "14", type: "Gene/ Genome Build Annotation"},
{atype: "15", type: "Standard QC"},
{atype: "16", type: "Standard RNA-Seq"},
{atype: "17", type: "Standard ChIP-Seq"}];

$scope.analysisProtocol = [{ptype: "1", type: "Whole_Exome_paired_end"},
{ptype: "2", type: "Whole genome_paired end"},
{ptype: "3", type: "RNAseq paired end"},
{ptype: "9", type: "Chip-seq_single_end"},
{ptype: "10", type: "RNAseq_single_end"}];

$scope.analysisOrganism = [{otype: "1", type: "Human"},
{otype: "2", type: "Yeast"},
{otype: "3", type: "Mouse"},
{otype: "4", type: "Drosophila melanogaster - Fruit Fly"},
{otype: "5", type: "Planaria"},
{otype: "6", type: "Celegans elegans"},
{otype: "7", type: "Newt"},
{otype: "8", type: "Xenopus tropicalis"},
{otype: "9", type: "Zebrafish"},
{otype: "10", type: "Rat"},
{otype: "11", type: "Arabidopsis"},
{otype: "13", type: "Gallus gallus - Chicken"},
{otype: "14", type: "S. pombe"},
{otype: "15", type: "Canis familiaris"},
{otype: "19", type: "Yeast Intergenic"},
{otype: "20", type: "Unknown"},
{otype: "21", type: "Sheep"},
{otype: "22", type: "Sodalis"},
{otype: "23", type: "Pig"},
{otype: "24", type: "Grape"},
{otype: "25", type: "Asteromyia carbonifera"},
{otype: "26", type: "Aspergillus fumigatus"},
{otype: "27", type: "Bos taurus - Cow"},
{otype: "28", type: "Alligator mississippiensis"},
{otype: "29", type: "Chrysemys picta - Turtle"},
{otype: "30", type: "Anolis carolinensis - Green Anole"},
{otype: "31", type: "Xenopus laevis"}];


$scope.analysisEditMode = false;
$scope.labSelected = "";
$scope.analysisSelected = "";
$scope.numberOfAnalyses = 0;
$scope.analyses = [];
$scope.projects = [];
$scope.analysisList = 'app/analysis/analysisList.html';
$scope.analysisDetailIL = 'app/analysis/analysisDetailIL.html';
$scope.analysisDetailEIL = 'app/analysis/analysisDetailEIL.html';
$scope.analysisDetailHS = 'app/analysis/analysisDetailHS.html';
$scope.analysisDetailDNA = 'app/analysis/analysisDetailDNA.html';
$scope.analysisForm = $scope.analysisList;
$scope.analysisSelected = null;
$scope.analysisSelectedIL = null;
$scope.visibilitySelected = "All Lab Members";
$scope.institutionSelected = "";
$scope.headingSelected = "";

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
	//console.log("[find] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group);
	$scope.showColumnForm = true;

    // do they want all of them?
    var whatToGet = "?allAnalysis=Y";
    if ($scope.searchFilter.selectAll) {
		whatToGet = "?allAnalysis=Y";
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

	//console.log("[find] *** calling getAnalysisGroupList, whatToGet: " + whatToGet);
	getAnalysisGroupList(whatToGet);
};

$scope.lookUp = function() {
	//console.log("[analysis lookUp] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group + " lookup: " + $scope.searchFilter.lookup);

	// get the analysis, if it exists, setup the analysisTree for the lab and display the analysis
	var analid = mapLookup($scope.searchFilter.lookup);
	lookupAnalysis(analid);
};

 $scope.$on('lookupAnalysis', function (event, args) {
	 $scope.searchFilter.lookup = args.message;
	 var analid = mapLookup($scope.searchFilter.lookup);
	 console.log("[analysis $on lookupAnalysis event] $scope.searchFilter.lookup " + $scope.searchFilter.lookup + " analid: " + analid);

	 // do the work
	 lookupAnalysis(analid);
 });

 	/*********************
 	 *
 	 * Watchers
 	 *
 	 ********************/
     $scope.$watch('whatToLookup',function() {
		console.log("[analysis whatToLookup watcher] $scope.whatToLookup: " + $scope.whatToLookup);
     	if ($scope.whatToLookup != null && $scope.whatToLookup != "") {
			$scope.searchFilter.lookup = $scope.whatToLookup;
			var analid = mapLookup($scope.searchFilter.lookup);
			lookupAnalysis(analid);
     	}
     });




     $scope.showSelected = function(sel) {
		//console.log("In showSelected " + sel.type + " " + sel.label + " " + $scope.analysisForm);
		$scope.showColumnForm = true;

         $scope.selectedNode = sel;
         if (sel.type == "L") {
		 	$scope.labSelected = sel.label;
		 	$scope.headingSelected = $scope.labSelected;
		 	$scope.analyses = [];

		 	$scope.projects = angular.copy(sel.children);

			// Go through each project and collect all the analyses
			//console.log("$scope.projects.length " + $scope.projects.length);
			for (var i=0; i<$scope.projects.length; i++) {
				//console.log("Processing project: " + $scope.projects[i].label);
				for (var j=0; j<$scope.projects[i].children.length; j++) {
					//console.log("Processing child: " + $scope.projects[i].children[j].label);
					$scope.analyses.push($scope.projects[i].children[j]);
				}
			}

		 	$scope.numberOfAnalyses = $scope.analyses.length;
		 	$scope.analysisForm = $scope.analysisList;
		}
		if (sel.type == "P") {
			$scope.projectSelected = sel.label;
		 	$scope.headingSelected = $scope.projectSelected;
			$scope.analyses = angular.copy(sel.children);
			$scope.numberOfAnalyses = $scope.analyses.length;
			$scope.analysisForm = $scope.analysisList;
		}
		if (sel.type == "E") {
			$scope.analysisSelected = angular.copy(sel);
			$scope.analyses = angular.copy(sel);
			$scope.numberOfAnalyses = $scope.analyses.length;
			if (sel.etype == "IL") {
			$scope.analysisForm = $scope.analysisDetailIL;
			}

			getFiles(sel.idAnalysis);
			getAnalysis(sel.idAnalysis);
			//console.log("In showSelected, label: " + $scope.analysisSelected.label + " number: " + $scope.analysisSelected.number + " etype: " + $scope.analysisSelected.etype);
		}
		//console.log("In showSelected, form: " + $scope.analysisForm);
     };

     $scope.afileSelectedHandler = function(branch) {
		console.log("In afileSelectedHandler " + branch.file);
	};


	function activate() {

		// get what we need from the $rootScope
		$scope.labs = angular.copy($rootScope.labs);
		console.log("[AnalysisController activate] $scope.labs.length: " + $scope.labs.length);
		$scope.okToPickLab = angular.copy($rootScope.okToPickLab);
		console.log("[AnalysisController activate] $scope.okToPickLab: " + $scope.okToPickLab);
		$scope.okToFind = angular.copy($rootScope.okToFind);
		console.log("[AnalysisController activate] $scope.okToFind: " + $scope.okToFind);
	};


     function getAnalysisGroupList(whatToGet) {
       var promise = AnalysisGroupListService.getAnalyses(whatToGet);

       promise.then(
          function(payload) {
              $scope.analysisRequestList = payload.data;

              // setup the analysis tree
              buildAnalysisTree();
          },
          function(errorPayload) {
              console.log('failure getAnalysisGroupList' + errorPayload);
          });
     };


     function getAnalysis(analid) {
       var promise = AnalysisService.getAnalysis(analid);

       promise.then(
          function(payload) {
              $scope.xmlAnalysis = payload.data;

              // parse out what we need for the screens
              parseAnalysis();
          },
          function(errorPayload) {
              console.log('failure getting Analysis' + errorPayload);
          });
     };

     function lookupAnalysis(analid) {
		 console.log("[lookupAnalysis] ** before getRequest ** analid: " + analid);
       var promise = AnalysisService.getAnalysis(analid);

       promise.then(
          function(payload) {
              $scope.xmlAnalysis = payload.data;

			  // don't display the initial column form list of analysis
			  $scope.showColumnForm = false;

			if (angular.isDefined($scope.xmlAnalysis.Analysis)) {

			  	// get the files
			  	getFiles(analid);

              	// parse out what we need for the screens
              	parseAnalysis();

              	var idLab = null;

              	// did we get the analysis?
              	if (angular.isDefined($scope.xmlAnalysis.Analysis)) {
					  idLab = $scope.xmlAnalysis.Analysis.idLab;
			  	}

			 	if (idLab != null) {
             	 	// get the analysis group list for the analysis lab
             	 	var whatToGet = "?idLab=" + idLab;
             	 	getAnalysisGroupList(whatToGet);
		    	}
			} // end of if we got something good

          },
          function(errorPayload) {
              console.log('failure looking up Analysis' + errorPayload);
          });
     };


     function getFiles(exprid) {
       var promise = AnalysisDownloadListService.getAnalysisDownloadList(exprid);

       promise.then(
          function(payload) {
              $scope.xmlFiles = payload.data;

              // *** note *** for now we just fake it
//              $scope.xmlFiles = angular.copy($scope.reqDownloadList);

              // parse out what we need for the Files tab
              parseFiles();
          },
          function(errorPayload) {
              console.log('failure getting AnalysisDownloadList' + errorPayload);
          });
     };


	 // temproary
	 function mapAnalType (anType) {
		 var mapped = anType;
		 for (var i=0;i<$scope.analysisType.length;i++) {
			 if (anType == $scope.analysisType[i].atype) {
				 mapped = $scope.analysisType[i].type;
				 break;
			 }
		 }; // end of for
		return mapped;
	 }; // end of mapAnalType

	 // temproary
	 function mapAnalProtocol (anType) {
		 var mapped = anType;
		 for (var i=0;i<$scope.analysisProtocol.length;i++) {
			 if (anType == $scope.analysisProtocol[i].ptype) {
				 mapped = $scope.analysisProtocol[i].type;
				 break;
			 }
		 }; // end of for
		return mapped;
	 }; // end of mapAnalProtocol

	 // temproary
	 function mapAnalOrganism (anType) {
		 var mapped = anType;
		 for (var i=0;i<$scope.analysisOrganism.length;i++) {
			 if (anType == $scope.analysisOrganism[i].otype) {
				 mapped = $scope.analysisOrganism[i].type;
				 break;
			 }
		 }; // end of for
		return mapped;
	 }; // end of mapAnalOrganism

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


	function mapLookup(requestNumber) {
		var analid = requestNumber;
		if (requestNumber == null || requestNumber == "") {
			//console.log("[analysis mapLookup] 1 returning analid: " + analid);
			return analid;
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
			//console.log("[analysis mapLookup] 2 returning analid: " + analid);
			return analid;		// nope
		}

		if (epos == -1) {
			analid = requestNumber.substring(spos);
		}
		else {
			analid = requestNumber.substring(spos,epos);
		}

		//console.log("[analysis mapLookup] 3 returning analid: " + analid + " requestNumber: " + requestNumber + " spos: " + spos + " epos: " + epos);
		return analid;

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

		$scope.atree_data = [];
		var aBranchTop = {file: "", view: "", comment: "", size: "", modified: "", children: []};

		aBranchTop.file = $scope.xmlFiles.Analysis.displayName;
		aBranchTop.comment = "";

		// if the FileDescriptor has a FileDescriptor then this is a directory
		if (angular.isDefined($scope.xmlFiles.Analysis.AnalysisFileDescriptor)) {

			// make sure it's an array
			var theFileDescriptors = [];

			if (!angular.isArray($scope.xmlFiles.Analysis.AnalysisFileDescriptor)) {
				theFileDescriptors.push($scope.xmlFiles.Analysis.AnalysisFileDescriptor);
			}
			else {
				for (var i=0; i<$scope.xmlFiles.Analysis.AnalysisFileDescriptor.length; i++) {
					theFileDescriptors.push($scope.xmlFiles.Analysis.AnalysisFileDescriptor[i]);
				}
			}
			//console.log("[parseFiles] Analysis theFileDescriptors.length: " + theFileDescriptors.length);

			// add these files to the top branch
			for (var i=0;i<theFileDescriptors.length;i++) {
				var aBranchFile = {file: "", view: "", comment: "", size: "", modified: "", children: []};

				aBranchFile.file = theFileDescriptors[i].displayName;
				aBranchFile.view = "";
				if (theFileDescriptors[i].viewURL != "") {
					aBranchFile.view = "/gnomexlite/" + theFileDescriptors[i].viewURL;
				}
				aBranchFile.comment = theFileDescriptors[i].comment;
				aBranchFile.size = theFileDescriptors[i].fileSizeText;
				aBranchFile.modified = theFileDescriptors[i].lastModifyDate;

				// if this FileDescriptor has a FileDescriptor then this is a directory
				if (angular.isDefined(theFileDescriptors[i].AnalysisFileDescriptor) ) {

					// make sure it's an array
					var theFileDescriptors1 = [];

					if (!angular.isArray(theFileDescriptors[i].AnalysisFileDescriptor)) {
						theFileDescriptors1.push(theFileDescriptors[i].AnalysisFileDescriptor);
					}
					else {
						for (var i1=0; i1<theFileDescriptors[i].AnalysisFileDescriptor.length; i1++) {
							theFileDescriptors1.push(theFileDescriptors[i].AnalysisFileDescriptor[i1]);
						}
					}
					//console.log("[parseFiles] Analysis theFileDescriptors1.length: " + theFileDescriptors1.length);

					// add these files to the containing branch
					for (var i1=0;i1<theFileDescriptors1.length;i1++) {
						var aBranchFile1 = {file: "", view: "", comment: "", size: "", modified: "", children: []};

						aBranchFile1.file = theFileDescriptors1[i1].displayName;
						aBranchFile1.view = "";
						if (theFileDescriptors1[i1].viewURL != "") {
							aBranchFile1.view = "/gnomexlite/" + theFileDescriptors1[i1].viewURL;
						}
						aBranchFile1.comment = theFileDescriptors1[i1].comment;
						aBranchFile1.size = theFileDescriptors1[i1].fileSizeText;
						aBranchFile1.modified = theFileDescriptors1[i1].lastModifyDate;

						// if this FileDescriptor1 has a FileDescriptor then this is a directory
						if (angular.isDefined(theFileDescriptors1[i1].AnalysisFileDescriptor) ) {

							// make sure it's an array
							var theFileDescriptors2 = [];

							if (!angular.isArray(theFileDescriptors1[i1].AnalysisFileDescriptor)) {
								theFileDescriptors2.push(theFileDescriptors1[i1].AnalysisFileDescriptor);
							}
							else {
								for (var i2=0; i2<theFileDescriptors1[i1].AnalysisFileDescriptor.length; i2++) {
									theFileDescriptors2.push(theFileDescriptors1[i1].AnalysisFileDescriptor[i2]);
								}
							}
							//console.log("[parseFiles] Analysis theFileDescriptors2.length: " + theFileDescriptors2.length);

							// add these files to the containing branch
							for (var i2=0;i2<theFileDescriptors2.length;i2++) {
								var aBranchFile2 = {file: "", view: "", comment: "", size: "", modified: "", children: []};

								aBranchFile2.file = theFileDescriptors2[i2].displayName;
								aBranchFile2.view = "";
								if (theFileDescriptors2[i2].viewURL != "") {
									aBranchFile2.view = "/gnomexlite/" + theFileDescriptors2[i2].viewURL;
								}
								aBranchFile2.comment = theFileDescriptors2[i2].comment;
								aBranchFile2.size = theFileDescriptors2[i2].fileSizeText;
								aBranchFile2.modified = theFileDescriptors2[i2].lastModifyDate;

								// if this FileDescriptor1 has a FileDescriptor then this is a directory
								if (angular.isDefined(theFileDescriptors2[i2].AnalysisFileDescriptor) ) {
									// ********** skip for now *************

								} // end of if theFileDescriptors2[i2].AnalysisFileDescriptor defined

							// save as a child of previous level
							aBranchFile1.children.push(aBranchFile2);

							} // end of for theFileDescriptors2.length

						} // end of if theFileDescriptors1[i1].AnalysisFileDescriptor defined

					// save as a child of previous level
					aBranchFile.children.push(aBranchFile1);

					} // end of for theFileDescriptors1.length

				} // end of if theFileDescriptors[i].AnalysisFileDescriptor defined

				// save it
				aBranchTop.children.push(aBranchFile);

			} // end of for theFileDescriptors.length

		} // end of if $scope.xmlFiles.Analysis.AnalysisFileDescriptor is defined

		$scope.atree_data.push(aBranchTop);

		//console.log("[parseFiles] *** leaving *** $scope.atree_data[0].file: " + $scope.atree_data[0].file);


	}; // end of parseFiles


	function parseAnalysis() {

		//console.log("[parseAnalysis] $scope.xmlAnalysis.length " + $scope.xmlAnalysis.length);

		// get the various (MANY) pieces separated out...
		// topics
		var theTopics = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.topics)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.topics)) {
				theTopics.push($scope.xmlAnalysis.Analysis.topics);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.topics.length; i++) {
					theTopics.push($scope.xmlAnalysis.Analysis.topics[i]);
				}
			}
		}
		//console.log("[parseAnalysis] theTopics.length: " + theTopics.length);

		// genomeBuilds
		var theGenomeBuilds = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.genomeBuilds)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.genomeBuilds)) {
				theGenomeBuilds.push($scope.xmlAnalysis.Analysis.genomeBuilds);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.genomeBuilds.length; i++) {
					theGenomeBuilds.push($scope.xmlAnalysis.Analysis.genomeBuilds[i]);
				}
			}
		}
		//console.log("[parseAnalysis] theGenomeBuilds.length: " + theGenomeBuilds.length);

		// setup organismName
		var theOrganismName = "";
		if (theGenomeBuilds.length > 0) {
			if (angular.isDefined(theGenomeBuilds[0].GenomeBuild.organism.Organism.display)) {
				theOrganismName = theGenomeBuilds[0].GenomeBuild.organism.Organism.display;
				//console.log("[ParseAnalysis] theOrganismName: " + theOrganismName);
			}
		}

		// setup genomeBuild
		var theGenomeBuild = "";
		if (theGenomeBuilds.length > 0) {
			if (angular.isDefined(theGenomeBuilds[0].GenomeBuild.display)) {
				theGenomeBuild = theGenomeBuilds[0].GenomeBuild.display;
				//console.log("[ParseAnalysis] theGenomeBuild: " + theGenomeBuild);
			}
		}


		// experimentItems
		var theExperimentItems = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.experimentItems)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.experimentItems)) {
				theExperimentItems.push($scope.xmlAnalysis.Analysis.experimentItems);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.experimentItems.length; i++) {
					theExperimentItems.push($scope.xmlAnalysis.Analysis.experimentItems[i]);
				}
			}
		}
		//console.log("[parseAnalysis] theExperimentItems.length: " + theExperimentItems.length);

		// analysisGroups
		var theAnalysisGroups = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.analysisGroups)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.analysisGroups)) {
				theAnalysisGroups.push($scope.xmlAnalysis.Analysis.analysisGroups);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.analysisGroups.length; i++) {
					theAnalysisGroups.push($scope.xmlAnalysis.Analysis.analysisGroups[i]);
				}
			}
		}
		//console.log("[parseAnalysis] theAnalysisGroups.length: " + theAnalysisGroups.length);

		// analysisProperties
		var theAnalysisProperties = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.analysisProperties)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.analysisProperties)) {
				theAnalysisProperties.push($scope.xmlAnalysis.Analysis.analysisProperties);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.analysisProperties.length; i++) {
					theAnalysisProperties.push($scope.xmlAnalysis.Analysis.analysisProperties[i]);
				}
			}
		}
		//console.log("[parseAnalysis] theAnalysisProperties.length: " + theAnalysisProperties.length);

		// setup structures to deal with analysisProperties.PropertyOption
		var analysisPropertyOptions = new Object();

		for (var i=0; i<theAnalysisProperties.length; i++) {
			if (!angular.isDefined(theAnalysisProperties[i].PropertyOption)) {
				continue;
			}

			// make PropertyOption an array
			var thePropertyOptions = [];

			if (!angular.isArray(theAnalysisProperties[i].PropertyOption)) {
				thePropertyOptions.push(theAnalysisProperties[i].PropertyOption);
			}
			else {
				for (var j=0; j<theAnalysisProperties[i].PropertyOption.length; j++) {
					thePropertyOptions.push(theAnalysisProperties[i].PropertyOption[j]);
				}
			}
			//console.log("[parseAnalysis] thePropertyOptions.length: " + thePropertyOptions.length);

			// can't happen
			if (thePropertyOptions.length == 0) {
				continue;
			}

			var propertyMap = new Object();

			for (var j=0; j<thePropertyOptions.length; j++) {
				propertyMap[thePropertyOptions[j].idPropertyOption] = thePropertyOptions[j].name;
			}; // end of for

			analysisPropertyOptions[theAnalysisProperties[i].idProperty] = angular.copy(propertyMap);
		}; // end of for


		// relatedObjects -- don't think it can ever be an array

		// things underneath relatedObjects
		var theRequests = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.relatedObjects.Request)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.relatedObjects.Request)) {
				theRequests.push($scope.xmlAnalysis.Analysis.relatedObjects.Request);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.relatedObjects.Request.length; i++) {
					theRequests.push($scope.xmlAnalysis.Analysis.relatedObjects.Request[i]);
				}
			}
		}
		//console.log("[parseAnalysis] relatedObjects theRequests.length: " + theRequests.length);

		// SequenceLane is under theRequests...

		// DataTrack
		var theDataTracks = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.relatedObjects.DataTrack)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.relatedObjects.DataTrack)) {
				theDataTracks.push($scope.xmlAnalysis.Analysis.relatedObjects.DataTrack);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.relatedObjects.DataTrack.length; i++) {
					theDataTracks.push($scope.xmlAnalysis.Analysis.relatedObjects.DataTrack[i]);
				}
			}
		}
		//console.log("[parseAnalysis] relatedObjects theDataTracks.length: " + theDataTracks.length);

		// relatedTopics
		var theRelatedTopics = [];

		if (angular.isDefined($scope.xmlAnalysis.Analysis.relatedTopics)) {
			if (!angular.isArray($scope.xmlAnalysis.Analysis.relatedTopics)) {
				theRelatedTopics.push($scope.xmlAnalysis.Analysis.relatedTopics);
			}
			else {
				for (var i=0; i<$scope.xmlAnalysis.Analysis.relatedTopics.length; i++) {
					theRelatedTopics.push($scope.xmlAnalysis.Analysis.relatedTopics[i]);
				}
			}
		}
		//console.log("[parseAnalysis] theRelatedTopics.length: " + theRelatedTopics.length);

		// need to do more parsing of things under relatedTopic

		// what we need for the analysisDetailInfo form
		var analSelIL = {requestor: "", name: "", submitterName: "", atype: "", protocol: "", codeVisibility: "", organismName: "", institution: "", collaborator: "", createDate: "", lastModifyDate: "", genomeBuilds: "", analysisGroupNames: ""};

		analSelIL.requestor = $scope.xmlAnalysis.Analysis.ownerName;
		analSelIL.name = $scope.xmlAnalysis.Analysis.name;
		analSelIL.submitterName = $scope.xmlAnalysis.Analysis.submitterName;
		analSelIL.atype = mapAnalType($scope.xmlAnalysis.Analysis.idAnalysisType);					// need to expand
		analSelIL.protocol = mapAnalProtocol($scope.xmlAnalysis.Analysis.idAnalysisProtocol);			// need to expand
		analSelIL.codeVisibility = mapVisibility($scope.xmlAnalysis.Analysis.codeVisibility);
		analSelIL.organismName = theOrganismName;
		analSelIL.institution = $scope.xmlAnalysis.Analysis.idInstitution;
		analSelIL.collaborator = "";
//		if ($scope.xmlAnalysis.Analysis.collaborators.length > 0) {
//			analSelIL.collaborator = $scope.xmlAnalysis.Analysis.collaborators[0];
//		}
		analSelIL.createDate = $scope.xmlAnalysis.Analysis.createDate;
		analSelIL.lastModifyDate = $scope.xmlAnalysis.Analysis.lastModifyDate;
		analSelIL.genomeBuilds = theGenomeBuild;
		analSelIL.analysisGroupNames = $scope.xmlAnalysis.Analysis.analysisGroupNames;

		$scope.analysisSelectedIL = angular.copy(analSelIL);

		// what we need for the description
		var analDesc = {desc: "", projdesc: "", notes: ""};

		//analDesc.desc = $scope.xmlAnalysis.Analysis.description;
		//analDesc.projdesc = $scope.xmlAnalysis.Analysis.projectDescription;
		//analDesc.notes = $scope.xmlAnalysis.Analysis.corePrepInstructions;

		$scope.selectedDescription = angular.copy(analDesc);

		// Build the labels and data for the Experiment Tab (if we have any)
		if (theExperimentItems.length > 0) {
			// set up the labels
			var sequenceLabels = [];

			sequenceLabels.push("#");					//0
			sequenceLabels.push("ID");					//1

			sequenceLabels.push("Experiment Name");		//2
			sequenceLabels.push("Sample Name");			//3
			sequenceLabels.push("Sample ID");			//4

			sequenceLabels.push("Seq Protocol");		//5
			sequenceLabels.push("Flow Cell #");			//6
			sequenceLabels.push("Channel");				//7

			sequenceLabels.push("Genome Build");		//8
			sequenceLabels.push("Analysis Intructions");	//9

			// remember column headers for the table
			$scope.selectedSequenceLabelsIL = angular.copy(sequenceLabels);

			// now get the data
			$scope.selectedSequencesIL = [];
			var theCount = 0;

			for (var i=0;i<theExperimentItems.length;i++) {
				theCount++;

				if (!angular.isDefined(theExperimentItems[i].sequenceLane.SequenceLane)) {
					continue;
				}

				var aSequence = new Object();
				aSequence[sequenceLabels[0]] = theCount;
				aSequence[sequenceLabels[1]] = theExperimentItems[i].sequenceLane.SequenceLane.number;
				aSequence[sequenceLabels[2]] = theExperimentItems[i].sequenceLane.SequenceLane.experimentName;
				aSequence[sequenceLabels[3]] = theExperimentItems[i].sequenceLane.SequenceLane.sampleName;
				aSequence[sequenceLabels[4]] = theExperimentItems[i].sequenceLane.SequenceLane.sampleNumber;

				aSequence[sequenceLabels[5]] = "";								// don't know where seq protocol is????

				aSequence[sequenceLabels[6]] = theExperimentItems[i].sequenceLane.SequenceLane.flowCellNumber;
				aSequence[sequenceLabels[7]] = theExperimentItems[i].sequenceLane.SequenceLane.flowCellChannelNumber;

				aSequence[sequenceLabels[8]] = theGenomeBuild; // haven't figured out where to get Custom Barcode B
				aSequence[sequenceLabels[9]] = theExperimentItems[i].sequenceLane.SequenceLane.analysisInstructions;

				// add it
				$scope.selectedSequencesIL.push(aSequence);
			}; // end of theExperimentItems.length for
		} // end of if theExperimentItems.length > 0

	}; // end of parseAnalysis



     function buildAnalysisTree () {

		//console.log("[buildAnalysisTree] $scope.analysisRequestList.length " + $scope.analysisRequestList.length);
		$scope.analysisTree = [];

		// make an array of labs
		var theLabs = [];

		if (!angular.isArray($scope.analysisRequestList.Lab)) {
			theLabs.push($scope.analysisRequestList.Lab);
		}
		else {
			for (var i=0; i<$scope.analysisRequestList.Lab.length; i++) {
				theLabs.push($scope.analysisRequestList.Lab[i]);
			}
		}
		//console.log("[buildAnalysisTree] theLabs.length: " + theLabs.length);

		// process the labs
		for (var i = 0; i < theLabs.length; i++) {
			//console.log("Processing Lab[" + i + "]: " + theLabs[i].label);

			var theLab = {type: "L", label: "", children: []};
			theLab.label = theLabs[i].label;

			// make an array out of AnalysisGroup
			var theAnalysisGroups = [];

			if (!angular.isArray(theLabs[i].AnalysisGroup)) {
				theAnalysisGroups.push(theLabs[i].AnalysisGroup);
			}
			else {
				for (var j=0; j<theLabs[i].AnalysisGroup.length; j++) {
					theAnalysisGroups.push(theLabs[i].AnalysisGroup[j]);
				}
			}
			//console.log("[buildAnalysisTree] Lab[" + i + "] theAnalysisGroups.length: " + theAnalysisGroups.length);

			// process the AnalysisGroups
			for (var j=0; j<theAnalysisGroups.length; j++) {
				//console.log("[buildAnalysisTree] Processing AnalysisGroup[" + j + "]: " + theAnalysisGroups[j].label);

				var theAnalysisGroup = {type: "P", label: "", children: []};
				theAnalysisGroup.label = theAnalysisGroups[j].label;

				// make an array out of Analysis
				var theAnalyses = [];

				// the AnalysisGroup may not have any Analysis
				if (angular.isDefined(theAnalysisGroups[j].Analysis) ) {
					if (!angular.isArray(theAnalysisGroups[j].Analysis)) {
						theAnalyses.push(theAnalysisGroups[j].Analysis);
					}
					else {
						for (var k=0; k<theAnalysisGroups[j].Analysis.length; k++) {
							theAnalyses.push(theAnalysisGroups[j].Analysis[k]);
						}
					}
				}

				//console.log("[buildAnalysisTree] AnalysisGroups[" + j + "] theAnalyses.length: " + theAnalyses.length);

				// process the Analysis
				for (var l=0; l<theAnalyses.length; l++) {
					//console.log("[buildAnalysisTree] Processing theAnalyses[" + l + "]: " + theAnalyses[l].idAnalysis);

						var theAnalysis = {type: "E", label: "", etype: "", idAnalysis: "", number: "", name: "", date: "", requestor: "", atype: "", protocol: "", organism: "", description: " ", visibility: "", children: [] };
						theAnalysis.label = theAnalyses[l].number + " - " + theAnalyses[l].name;

						// shorten it
						if (theAnalysis.label.length > 31) {
							theAnalysis.label = theAnalysis.label.substr(0, 31);
						}


						// set up analysis type
						theAnalysis.etype = "IL";

						theAnalysis.idAnalysis = theAnalyses[l].idAnalysis;
						theAnalysis.number = theAnalyses[l].number;
						theAnalysis.name = theAnalyses[l].name;
						theAnalysis.date = theAnalyses[l].createDateDisplay;
						theAnalysis.requestor = theAnalyses[l].ownerName;
						theAnalysis.atype = mapAnalType(theAnalyses[l].idAnalysisType);					// need to lookup
						theAnalysis.protocol = mapAnalProtocol(theAnalyses[l].idAnalysisProtocol);			// need to lookup
						theAnalysis.organism = mapAnalOrganism(theAnalyses[l].idOrganism);					// need to lookup
						theAnalysis.description = parseText(theAnalyses[l].description);
						theAnalysis.visibility = "All Lab Members";							// need to find

						theAnalysisGroup.children.push(theAnalysis);
					} // end of theAnalyses for

				theLab.children.push(theAnalysisGroup);
			} // end of theAnalysisGroups for

			$scope.analysisTree.push(theLab);
		} // end of theLabs for

		// make the tree be expanded by default
		for (var i = 0; i<$scope.analysisTree.length; i++) {
			$scope.expandedAnalysisTree.push($scope.analysisTree[i]);

			for (var j = 0;j<$scope.analysisTree[i].children.length;j++) {
				$scope.expandedAnalysisTree.push($scope.analysisTree[i].children[j]);
			}
		}

	   	// automatically setup the initial analysis list for the first lab
	  if ($scope.showColumnForm) {
	   	var sel = angular.copy($scope.analysisTree[0]);
         $scope.selectedNode = sel;
         if (sel.type == "L") {
		 	$scope.labSelected = sel.label;
		 	$scope.headingSelected = $scope.labSelected;
		 	$scope.analyses = [];

		 	$scope.projects = angular.copy(sel.children);

			// Go through each project and collect all the analyses
			//console.log("$scope.projects.length " + $scope.projects.length);
			for (var i=0; i<$scope.projects.length; i++) {
				//console.log("Processing project: " + $scope.projects[i].label);
				for (var j=0; j<$scope.projects[i].children.length; j++) {
					//console.log("Processing child: " + $scope.projects[i].children[j].label);
					$scope.analyses.push($scope.projects[i].children[j]);
				}
			}

		 	$scope.numberOfAnalyses = $scope.analyses.length;
		 	$scope.analysisForm = $scope.analysisList;
		}
	} else {
		// we got here doing a Lookup, pretend someone clicked on the analysis of interest...
	     var sel = angular.copy ($scope.analysisTree[0]);
         $scope.selectedNode = sel;
         if (sel.type == "L") {
		 	$scope.labSelected = sel.label;
		 	$scope.headingSelected = $scope.labSelected;
		 	$scope.analyses = [];

		 	$scope.projects = angular.copy(sel.children);

			// Go through each project and find the analysis
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
						$scope.analysisSelected = angular.copy(sel);
						$scope.analyses = angular.copy(sel);
						$scope.numberOfAnalyses = $scope.analyses.length;
						if (sel.etype == "IL") {
							$scope.analysisForm = $scope.analysisDetailIL;
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

	 }; // end of buildAnalysisTree

}]);





