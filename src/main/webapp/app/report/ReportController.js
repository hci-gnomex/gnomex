'use strict';

/**
 * ReportController
 * @constructor
 */

var report    = angular.module('report', ['ui.bootstrap','treeControl','treeGrid', 'filters', 'services', 'directives','chosen','ngProgress','dialogs.main','error']);
console.log("In ReportController.js");

angular.module("report")
  .factory('AnnotationProgressReportService', function($http) {
    return {
      getAnnotationProgressReport: function(whatToGet) {
         return $http.get('/gnomexlite/ShowAnnotationProgressReport.gx' + whatToGet, { responseType: "arraybuffer" });
      }
    }
  })
  .factory('AnnotationReportService', function($http) {
    return {
      getAnnotationReport: function(whatToGet) {
         return $http.get('/gnomexlite/ShowAnnotationReport.gx' + whatToGet);
      }
    }
  })

.controller("ReportController", [
'$scope', '$http', '$modal','$rootScope','$q','$filter','ngProgress','dialogs','AnnotationProgressReportService','AnnotationReportService',
function($scope, $http, $modal, $rootScope, $q, $filter, ngProgress, dialogs, AnnotationProgressReportService, AnnotationReportService) {
	/**********************
	 * Initialization!
	 *********************/
console.log("In ReportController.js 2");
$scope.reportList = "";
$scope.okToFind = true;						// comes from $rootScope
$scope.okToPickLab = true;					// comes from $rootScope
$scope.whatToLookup = $rootScope.whatReportToLookup;
$scope.showColumnForm = true;

$scope.searchFilter = {selectAll: false, group: "", owner: "", lookup: ""};

$scope.labs = [];							// comes from $rootScope
$scope.reportTree = [];
$scope.expandedReportTree = [];

$scope.selectedDescription = {};
$scope.reportSelected = {};

$scope.reportEditMode = false;
$scope.labSelected = "";

$scope.reportList = 'app/report/reportFolderDetail.html';
$scope.reportDetail = 'app/report/reportDetail.html';
$scope.reportFolderDetail = 'app/report/reportFolderDetail.html';

$scope.reportForm = $scope.reportList;

$scope.reportSelected = [];

$scope.visibilitySelected = "All Lab Members";
$scope.institutionSelected = "";
$scope.headingSelected = "";
$scope.labName = "";

// start the world
activate();

$scope.runAnnotationProgress = function() {
	console.log("[runAnnotationProgress] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group);
	$scope.showColumnForm = true;

    // do they want all of them?
    var whatToGet = "?allReport=Y";
    if ($scope.searchFilter.selectAll) {
		whatToGet = "?allReport=Y";
	}
	else {
		// specific lab
		if (angular.isDefined($scope.searchFilter.group.name)) {
			//console.log("[runAnnotationProgress] looking for " + $scope.searchFilter.group.name);
			// get the lab id
			for (var i = 0; i<$scope.labs.length; i++) {
				if ($scope.searchFilter.group.name == $scope.labs[i].name) {
					whatToGet = "?idLab=" + $scope.labs[i].idLab;
					$scope.labName = $scope.labs[i].lastName;
					break;
				}
			}
		}
	}

	console.log("[runAnnotationProgress] *** calling getAnnotationProgressReport, whatToGet: " + whatToGet);
	getAnnotationProgressReport(whatToGet);
};


	function activate() {

		// get what we need from the $rootScope
		$scope.labs = angular.copy($rootScope.labs);
		console.log("[ReportController activate] $scope.labs.length: " + $scope.labs.length);
		$scope.okToPickLab = angular.copy($rootScope.okToPickLab);
		console.log("[ReportController activate] $scope.okToPickLab: " + $scope.okToPickLab);
		$scope.okToFind = angular.copy($rootScope.okToFind);
		console.log("[ReportController activate] $scope.okToFind: " + $scope.okToFind);
	};

     function getAnnotationProgressReport(whatToGet) {
       var promise = AnnotationProgressReportService.getAnnotationProgressReport(whatToGet);

       promise.then(
          function(payload) {
			  // when we get here, we've got the binary spreadsheet data
              $scope.reportList = payload.data;
              console.log("[getAnnotationProgressReport] data byte length: " + $scope.reportList.byteLength);
              console.log("[getAnnotationProgressReport] data length: " + $scope.reportList.length);

              var date = new Date();
			  $scope.ymd = $filter('date')(new Date(), 'yyyy_MM_dd');
			  console.log("[getAnnotationProgressReport] filename: " + 'gnomex_annotation_progress__' + $scope.labName + '_' + $scope.ymd + '.xlsx');

			  var blob = new Blob([payload.data], {
        	  type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    		  });
    		  saveAs(blob, 'gnomex_annotation_progress__' + $scope.labName + '_' + $scope.ymd + '.xlsx');

          },
          function(errorPayload) {
              console.log('failure getAnnotationProgressReport' + errorPayload);
          });
     };


}]);





