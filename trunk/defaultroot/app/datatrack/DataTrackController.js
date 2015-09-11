'use strict';

/**
 * DataTrackController
 * @constructor
 */

var datatrack    = angular.module('datatrack', ['ui.bootstrap','treeControl','treeGrid', 'filters', 'services', 'directives','chosen','ngProgress','dialogs.main','error']);
console.log("In DataTrackController.js");

angular.module("datatrack")
  .factory('DataTrackService', function($http) {
    return {
      getDataTrack: function(dtid) {
         return $http.get('/gnomexlite/GetDataTrack.gx?idDataTrack=' + dtid);
      }
    }
  })
  .factory('DataTrackListService', function($http) {
    return {
      getDataTrackList: function(whatever) {
         return $http.get('/gnomexlite/GetDataTrackList.gx');
      }
    }
  })
  .factory('GenomeBuildService', function($http) {
    return {
      getGenomeBuild: function(gbid) {
         return $http.get('/gnomexlite/GetGenomeBuild.gx?idGenomeBuild=' + gbid);
      }
    }
  })

.controller("DataTrackController", [
'$scope', '$http', '$modal','$rootScope','$q','ngProgress','dialogs','DataTrackService','DataTrackListService','GenomeBuildService',
function($scope, $http, $modal, $rootScope, $q, ngProgress, dialogs, DataTrackService, DataTrackListService, GenomeBuildService) {
	/**********************
	 * Initialization!
	 *********************/
console.log("In DataTrackController.js 2");
$scope.xmlDataTrack = "";
$scope.xmlGenomeBuild = "";
$scope.datatrackList = "";
$scope.okToFind = true;						// comes from $rootScope
$scope.okToPickLab = true;					// comes from $rootScope
$scope.whatToLookup = $rootScope.whatDataTrackToLookup;
$scope.showColumnForm = true;

$scope.searchFilter = {selectAll: false, group: "", owner: "", lookup: ""};

$scope.labs = [];
$scope.datatrackTree = [];
$scope.expandedDataTrackTree = [];

$scope.selectedDescription = {};

$scope.selectedSegmentLabels = [];
$scope.selectedSegments = [];

$scope.selectedSeqFileLabels = [];
$scope.selectedSeqFiles = [];

$scope.xmlFiles = "";
$scope.tree_data = [];
$scope.col_defs = [];
$scope.expanding_property = {};

$scope.organismSelected= {};
$scope.genomeBuildSelected = {};
$scope.genomeBuildSelected1 = {};
$scope.folderSelected = {};
$scope.datatrackSelected = {};

// fake file data
$scope.reqDownloadList = {};

$scope.datatrackEditMode = false;
$scope.labSelected = "";

$scope.numberOfDataTracks = 0;
$scope.organism = [];
$scope.genomeBuild = [];
$scope.projects = [];
$scope.datatrackList = 'app/datatrack/datatrackList.html';
$scope.datatrackDetail = 'app/datatrack/datatrackDetail.html';

$scope.organismDetail = 'app/datatrack/organismDetail.html';
$scope.genomebuildDetail = 'app/datatrack/genomebuildDetail.html';
$scope.folderDetail = 'app/datatrack/folderDetail.html';

$scope.datatrackForm = $scope.datatrackList;

$scope.selectedDataTrackFileLabels = [];
$scope.selectedDataTrackFiles = [];

$scope.selectedDataTrackAnnotationLabels = [];
$scope.selectedDataTrackAnnotations = [];

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
    var whatToGet = "?allDataTrack=Y";
    if ($scope.searchFilter.selectAll) {
		whatToGet = "?allDataTrack=Y";
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

	//console.log("[find] *** calling getDataTrackList, whatToGet: " + whatToGet);
	getDataTrackList(whatToGet);
};

$scope.lookUp = function() {
	//console.log("[datatrack lookUp] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group + " lookup: " + $scope.searchFilter.lookup);

	// get the datatrack, if it exists, setup the datatrackTree for the lab and display the datatrack
	var dtid = mapLookup($scope.searchFilter.lookup);
	lookupDataTrack(dtid);
};

 $scope.$on('lookupDataTrack', function (event, args) {
	 $scope.searchFilter.lookup = args.message;
	 var dtid = mapLookup($scope.searchFilter.lookup);
	 console.log("[datatrack $on lookupDataTrack event] $scope.searchFilter.lookup " + $scope.searchFilter.lookup + " dtid: " + dtid);

	 // do the work
	 lookupDataTrack(dtid);
 });

 	/*********************
 	 * Watchers
 	 ********************/
     $scope.$watch('whatToLookup',function() {
		console.log("[datatrack whatToLookup watcher] $scope.whatToLookup: " + $scope.whatToLookup);
     	if ($scope.whatToLookup != null && $scope.whatToLookup != "") {
			$scope.searchFilter.lookup = $scope.whatToLookup;
			var dtid = mapLookup($scope.searchFilter.lookup);
			lookupDataTrack(dtid);
     	}
     });



     $scope.showSelected = function(sel) {
		//console.log("In showSelected " + sel.type + " " + sel.label + " " + $scope.datatrackForm);
		$scope.showColumnForm = true;

         $scope.selectedNode = sel;
         if (sel.type == "O") {
		 	$scope.organismSelected = angular.copy(sel);
		 	$scope.datatrackForm = $scope.organismDetail;
		}
         if (sel.type == "G") {
		 	$scope.genomeBuildSelected1 = angular.copy(sel);
		 	$scope.datatrackForm = $scope.genomebuildDetail;

		 	getGenomeBuild(sel.idGenomeBuild);
		}
		if (sel.type == "F") {
			$scope.folderSelected = angular.copy(sel);
			$scope.datatrackForm = $scope.folderDetail;
		}
		if (sel.type == "D") {
			$scope.datatrackSelected = angular.copy(sel);
			$scope.datatrackForm = $scope.datatrackDetail;

			getDataTrack(sel.idDataTrack);
			//console.log("In showSelected "D", label: " + $scope.datatrackSelected.label);
		}
		//console.log("In showSelected, form: " + $scope.datatrackForm);
     };


	function activate() {

		// get what we need from the $rootScope
		$scope.labs = angular.copy($rootScope.labs);
		console.log("[DataTrackController activate] $scope.labs.length: " + $scope.labs.length);
		$scope.okToPickLab = angular.copy($rootScope.okToPickLab);
		console.log("[DataTrackController activate] $scope.okToPickLab: " + $scope.okToPickLab);
		$scope.okToFind = angular.copy($rootScope.okToFind);
		console.log("[DataTrackController activate] $scope.okToFind: " + $scope.okToFind);
	};


     function getDataTrackList(whatToGet) {
       var promise = DataTrackListService.getDataTrackList(whatToGet);

       promise.then(
          function(payload) {
              $scope.datatrackList = payload.data;

              // setup the datatrack tree
              buildDataTrackTree();
          },
          function(errorPayload) {
              console.log('failure getDataTrackList' + errorPayload);
          });
     };


     function getDataTrack(dtid) {
       var promise = DataTrackService.getDataTrack(dtid);

       promise.then(
          function(payload) {
             $scope.xmlDataTrack = payload.data;

			 // make sure we got something
			 if (angular.isDefined($scope.xmlDataTrack.Files.Dir.File)) {
              	// parse out what we need for the screens
              	parseDataTrack();
			}

          },
          function(errorPayload) {
              console.log('failure getting DataTrack' + errorPayload);
          });
     };


     function lookupDataTrack(dtid) {
		 console.log("[lookupDataTrack] ** before getDataTrack ** dtid: " + dtid);
       var promise = DataTrackService.getDataTrack(dtid);

       promise.then(
          function(payload) {
              $scope.xmlDataTrack = payload.data;

			  // don't display the default form for datatrack
			  $scope.showColumnForm = false;

			if (angular.isDefined($scope.xmlDataTrack.Files.Dir.File)) {

              	// parse out what we need for the screens
              	parseDataTrack();

				var whatToGet = "?allDataTrack=Y";
				getDataTrackList(whatToGet);
			} // end of if we got something good

          },
          function(errorPayload) {
              console.log('failure looking up Data Track' + errorPayload);
          });
     };


     function getGenomeBuild(gbid) {
       var promise = GenomeBuildService.getGenomeBuild(gbid);

       promise.then(
          function(payload) {
              $scope.xmlGenomeBuild = payload.data;

              // parse out what we need for the screens
              parseGenomeBuild();
          },
          function(errorPayload) {
              console.log('failure getting GenomeBuild' + errorPayload);
          });
     };

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

	function mapLookup(requestNumber) {
		var dtid = requestNumber;
		if (requestNumber == null || requestNumber == "") {
			//console.log("[datatrack mapLookup] 1 returning dtid: " + dtid);
			return dtid;
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
			//console.log("[datatrack mapLookup] 2 returning dtid: " + dtid);
			return dtid;		// nope
		}

		if (epos == -1) {
			dtid = requestNumber.substring(spos);
		}
		else {
			dtid = requestNumber.substring(spos,epos);
		}

		//console.log("[datatrack mapLookup] 3 returning dtid: " + dtid + " requestNumber: " + requestNumber + " spos: " + spos + " epos: " + epos);
		return dtid;

	}; // end of mapLookup

	function parseDataTrack() {

		//console.log("[parseDataTrack] $scope.xmlDataTrack.length " + $scope.xmlDataTrack.length);

		// make the files an array
		var theFiles = [];

		if (angular.isDefined($scope.xmlDataTrack.Files.Dir.File)) {
			if (!angular.isArray($scope.xmlDataTrack.Files.Dir.File)) {
				theFiles.push($scope.xmlDataTrack.Files.Dir.File);
			}
			else {
				for (var i=0; i<$scope.xmlDataTrack.Files.Dir.File.length; i++) {
					theFiles.push($scope.xmlDataTrack.Files.Dir.File[i]);
				}
			}
		}
		//console.log("[parseDataTrack] theFiles.length: " + theFiles.length);

		// set up the labels
		var fileLabels = [];

		fileLabels.push("Name");				//0
		fileLabels.push("Date");				//1
		fileLabels.push("Size");				//2
		fileLabels.push("Analysis");			//3

		// remember column headers for the table
		$scope.selectedDataTrackFileLabels = angular.copy(fileLabels);

		// now get the data
		$scope.selectedDataTrackFiles = [];

		for (var i=0;i<theFiles.length;i++) {

			var aFile = new Object();
			aFile[fileLabels[0]] = theFiles[i].name;
			aFile[fileLabels[1]] = theFiles[i].lastModifed;
			aFile[fileLabels[2]] = theFiles[i].size;
			aFile[fileLabels[3]] = theFiles[i].analysisLabel;

			// add it
			$scope.selectedDataTrackFiles.push(aFile);

		} // end of for theFiles.length

		// get the information for the annotations


	}; // end of parseDataTrack



     function buildDataTrackTree () {

		//console.log("[buildDataTrackTree] $scope.datatrackList.length " + $scope.datatrackList.length);
		$scope.datatrackTree = [];

		// make an array of organisms
		var theOrganisms = [];

		if (!angular.isArray($scope.datatrackList.Organism)) {
			theOrganisms.push($scope.datatrackList.Organism);
		}
		else {
			for (var i=0; i<$scope.datatrackList.Organism.length; i++) {
				theOrganisms.push($scope.datatrackList.Organism[i]);
			}
		}
		//console.log("[buildDataTrackTree] theOrganisms.length: " + theOrganisms.length);

		// process the organisms
		for (var i = 0; i < theOrganisms.length; i++) {
			//console.log("Processing Organism[" + i + "]: " + theOrganisms[i].label);

			var theOrganism = {type: "O", label: "", idOrganism: "", commonName: "", binomialName: "", DAS2name: "", NCBITaxID: "", isActive: "", children: []};
			theOrganism.label = theOrganisms[i].label;
			theOrganism.idOrganism = theOrganisms[i].idOrganism;
			theOrganism.commonName = theOrganisms[i].commonName;
			theOrganism.binomialName = theOrganisms[i].binomialName;
			theOrganism.DAS2name = theOrganisms[i].name;
			theOrganism.NCBITaxID = theOrganisms[i].NCBITaxID;
			theOrganism.isActive = theOrganisms[i].isActive;

			// is there a GenomeBuild?
			if (!angular.isDefined(theOrganisms[i].GenomeBuild)) {
				continue;
			}
			
			// make an array out of GenomeBuild
			var theGenomeBuilds = [];

			if (!angular.isArray(theOrganisms[i].GenomeBuild)) {
				theGenomeBuilds.push(theOrganisms[i].GenomeBuild);
			}
			else {
				for (var j=0; j<theOrganisms[i].GenomeBuild.length; j++) {
					theGenomeBuilds.push(theOrganisms[i].GenomeBuild[j]);
				}
			}
			//console.log("[buildDataTrackTree] GenomeBuild[" + i + "] theGenomeBuilds.length: " + theGenomeBuilds.length);

			// process the GenomeBuilds
			for (var j=0; j<theGenomeBuilds.length; j++) {
				//console.log("[buildDataTrackTree] Processing GenomeBuild[" + j + "]: " + theGenomeBuilds[j].label);

				var theGenomeBuild = {type: "G", label: "", idGenomeBuild: "", children: []};
				theGenomeBuild.label = theGenomeBuilds[j].label;
				theGenomeBuild.idGenomeBuild = theGenomeBuilds[j].idGenomeBuild;

				// make an array out of DataTrackFolder
				var theDataTrackFolders = [];

				// the GenomeBuild may not have any DataTrackFolders
				if (angular.isDefined(theGenomeBuilds[j].DataTrackFolder) ) {
					if (!angular.isArray(theGenomeBuilds[j].DataTrackFolder)) {
						theDataTrackFolders.push(theGenomeBuilds[j].DataTrackFolder);
					}
					else {
						for (var k=0; k<theGenomeBuilds[j].DataTrackFolder.length; k++) {
							theDataTrackFolders.push(theGenomeBuilds[j].DataTrackFolder[k]);
						}
					}
				}

				//console.log("[buildDataTrackTree] Top Level DataTrackFolders[" + j + "] theDataTrackFolders.length: " + theDataTrackFolders.length);

				// process the DataTrackFolders
				for (var l=0; l<theDataTrackFolders.length; l++) {
					//console.log("[buildDataTrackTree] Processing theDataTrackFolders[" + l + "]: " + theDataTrackFolders[l].label);

					var theDataTrackFolder = processFolder(theDataTrackFolders[l]);

					// we are at the top folder level so add as child of theGenomeBuild
					theGenomeBuild.children.push(theDataTrackFolder);
				} // end of theDataTrackFolders for

				theOrganism.children.push(theGenomeBuild);
			} // end of theGenomeBuilds for

			$scope.datatrackTree.push(theOrganism);
		} // end of theOrganisms for

		// make the tree be expanded by default
		for (var i = 0; i<$scope.datatrackTree.length; i++) {
			$scope.expandedDataTrackTree.push($scope.datatrackTree[i]);

			for (var j = 0;j<$scope.datatrackTree[i].children.length;j++) {
				$scope.expandedDataTrackTree.push($scope.datatrackTree[i].children[j]);
			}
		}

	  if (!$scope.showColumnForm) {
		  // we got here doing a Lookup, pretend someone clicked on the data track of interest...
		  var foundIT = false;

		  for (var i = 0; i<$scope.datatrackTree.length; i++) {

			for (var j = 0;j<$scope.datatrackTree[i].children.length;j++) {
				// we will always find it because we already successfully got the data track data
				var sel = findADataTrack ($scope.datatrackTree[i].children[j],$scope.searchFilter.lookup);

				if (sel.type == "D" && sel.number == $scope.searchFilter.lookup) {
					$scope.datatrackSelected = angular.copy(sel);
					$scope.datatrackForm = $scope.datatrackDetail;

					foundIT = true;
					break;
				}
			} // end of for j

			// are we done?
			if (foundIT) {
				break;
			}
		} // end of for i
	  } // end of if !$scope.showColumnForm

	 }; // end of buildDataTrackTree


	 // walk the datatrackTree until we find the data track of interest
	 function findADataTrack (node,dtid) {

		 // just in case...
		 // is it a data track? and the one we want?
		 if (node.type == "D" && node.number == dtid) {
			// yes, give it back
			return node;
		 }


		 for (var i=0;i<=node.children.length;i++) {
			 // is it a data track?
			 var child = angular.copy(node.children[i]);

			 // is it a data track? and the one we want?
			 if (child.type == "D" && child.number == dtid) {
				 // yes, give it back
				 return child;
			 }

			 // if no children we are done with this child
			 if (child.children.length == 0) {
				 continue;
			 }

			 // down we go
			 var result = findADataTrack (child,dtid);

			 if (result == null) {
				 // nothing down there of interest
				 continue;
			 }

			 // is it a data track? and the one we want?
			 if (result.type == "D" && result.number == dtid) {
				 // yes, give it back
				 return result;
			 }
		 } // end of for i

		 // walked all the way to bottom of this branch and found nothing
		 return null;

	 }; // end of findADataTrack

	 // recursively call ourselves until all the nested folders have been dealt with
     function processFolder (aFolder) {
		//console.log("[processFolder] *** starting *** aFolder.label " + aFolder.label);

		var theDataTrackFolder = {type: "F", label: "", etype: "", idDataTrackFolder: "", name: "", lab: "", description: "", createdBy: "", createDate: "", genomeBuild: "", children: [] };
		theDataTrackFolder.label = aFolder.label;

		// shorten it
		if (theDataTrackFolder.label.length > 31) {
			theDataTrackFolder.label = theDataTrackFolder.label.substr(0, 31);
		}

		// set up DataTrackFolder type
		theDataTrackFolder.etype = "F";
		theDataTrackFolder.idDataTrackFolder = aFolder.idDataTrackFolder;
		theDataTrackFolder.name = aFolder.name;
		theDataTrackFolder.lab = aFolder.lab;
		theDataTrackFolder.description = parseText(aFolder.description);
		theDataTrackFolder.createdBy = aFolder.createdBy;
		theDataTrackFolder.createDate = aFolder.createDate;
		theDataTrackFolder.genomeBuild = aFolder.genomeBuild;

		// is there a nested folder?
		if (angular.isDefined(aFolder.DataTrackFolder) ) {

			// make sure it's an array
			var nestedDataTrackFolders = [];

			if (!angular.isArray(aFolder.DataTrackFolder)) {
				nestedDataTrackFolders.push(aFolder.DataTrackFolder);
			}
			else {
				for (var k=0; k<aFolder.DataTrackFolder.length; k++) {
					nestedDataTrackFolders.push(aFolder.DataTrackFolder[k]);
				}
			}

			//console.log("[processFolder] in folder: " + aFolder.label + " nestedDataTrackFolders.length: " + nestedDataTrackFolders.length);

			// process the DataTrackFolders
			for (var l=0; l<nestedDataTrackFolders.length; l++) {
				//console.log("[buildDataTrackTree] Processing nestedDataTrackFolders[" + l + "]: " + nestedDataTrackFolders[l].label);

				var theNestedDataTrackFolder = processFolder(nestedDataTrackFolders[l]);

				// add it as a child
				theDataTrackFolder.children.push(theNestedDataTrackFolder);

			} // end of for nestedDataTrackFolders

			// if we get here we have already dealt with the data tracks in some nested folder...
			// so we're done
			return theDataTrackFolder;

		} // end of if aFolder has a DataTrackFolder

		// Does this folder contain a data track?
		if (angular.isDefined(aFolder.DataTrack) ) {
			// we finally got to the end of nested folders...

			// make an array out of DataTrack
			var theDataTracks = [];

			if (!angular.isArray(aFolder.DataTrack)) {
				theDataTracks.push(aFolder.DataTrack);
			}
			else {
				for (var m=0; m<aFolder.DataTrack.length; m++) {
					theDataTracks.push(aFolder.DataTrack[m]);
				}
			}

			//console.log("[processFolder] inside folder: " + aFolder.label + " theDataTracks.length: " + theDataTracks.length);

			// process the DataTracks
			for (var m=0; m<theDataTracks.length; m++) {
				//console.log("[processFolder] Processing theDataTracks[" + m + "]: " + theDataTracks[m].label);

				var theDataTrack = {type: "D", label: "", etype: "", idDataTrack: "", number: "", name: "", lab: "", description: "", createdBy: "", createDate: "", genomeBuild: "", summary: "", codeVisibility: "", owner: "", children: [] };
				theDataTrack.label = theDataTracks[m].label;

				// shorten it
				if (theDataTrack.label.length > 31) {
					theDataTrack.label = theDataTrack.label.substr(0, 31);
				}

				// set up DataTrack type
				theDataTrack.etype = "D";

				theDataTrack.idDataTrack = theDataTracks[m].idDataTrack;
				theDataTrack.number = theDataTracks[m].number;
				theDataTrack.name = theDataTracks[m].name;
				theDataTrack.lab = theDataTracks[m].securityGroup;
				theDataTrack.description = parseText(theDataTracks[m].description);
				theDataTrack.createdBy = theDataTracks[m].createdBy;
				theDataTrack.createDate = theDataTracks[m].createDate;
				theDataTrack.genomeBuild = theDataTracks[m].genomeBuild;
				theDataTrack.summary = theDataTracks[m].summary;
				theDataTrack.codeVisibility = mapVisibility(theDataTracks[m].codeVisibility);
				theDataTrack.owner = theDataTracks[m].owner;

				// add it as a child of the DataTrackFolder
				theDataTrackFolder.children.push(theDataTrack);
			} // end of for theDataTracks

		} // end of if aFolder.DataTrack is defined

		// return the folder object we build
		return theDataTrackFolder;

	}; // end of processFolder

     function parseGenomeBuild () {
		//console.log("[parseGenomeBuild] *** starting ***");

		$scope.genomeBuildSelected = {das2Name: "", genomeBuildName: "", buildDate: "", ucscName: "", igvName: "", coordURI: "", coordVersion: "", coordSource: "", coordTestRange: "", coordAuthority: "", isActive: ""};

		// fill it in
		$scope.genomeBuildSelected.das2Name = $scope.xmlGenomeBuild.das2Name;
		$scope.genomeBuildSelected.genomeBuildName = $scope.xmlGenomeBuild.genomeBuildName;
		$scope.genomeBuildSelected.buildDate = $scope.xmlGenomeBuild.buildDate;
		$scope.genomeBuildSelected.ucscName = $scope.xmlGenomeBuild.ucscName;
		$scope.genomeBuildSelected.igvName = $scope.xmlGenomeBuild.igvName;
		$scope.genomeBuildSelected.coordURI = $scope.xmlGenomeBuild.coordURI;
		$scope.genomeBuildSelected.coordVersion = $scope.xmlGenomeBuild.coordVersion;
		$scope.genomeBuildSelected.coordSource = $scope.xmlGenomeBuild.coordSource;
		$scope.genomeBuildSelected.coordTestRange = $scope.xmlGenomeBuild.coordTestRange;
		$scope.genomeBuildSelected.coordAuthority = $scope.xmlGenomeBuild.coordAuthority;
		$scope.genomeBuildSelected.isActive = $scope.xmlGenomeBuild.isActive;

		// make an array out of Segments
		var theSegments = [];

		if (!angular.isArray($scope.xmlGenomeBuild.Segments)) {
			theSegments.push($scope.xmlGenomeBuild.Segments);
		}
		else {
			for (var m=0; m<$scope.xmlGenomeBuild.Segments.length; m++) {
				theSegments.push($scope.xmlGenomeBuild.Segments[m]);
			}
		}

		//console.log("[parseGenomeBuild] theSegments.length: " + theSegments.length);

		// setup the labels and data for the segments
		var segmentLabels = [];

		segmentLabels.push("Name");						//0
		segmentLabels.push("Length");					//1
		segmentLabels.push("Order");					//2

		// remember column headers for the table
		$scope.selectedSegmentLabels = angular.copy(segmentLabels);

		// now get the data
		$scope.selectedSegments = [];

		for (var i=0;i<theSegments.length;i++) {

			var aSegment = new Object();
			aSegment[segmentLabels[0]] = theSegments[i].name;
			aSegment[segmentLabels[1]] = theSegments[i].length;
			aSegment[segmentLabels[2]] = theSegments[i].sortOrder;

			// add it
			$scope.selectedSegments.push(aSegment);
		}; // end of theSegments.length for


		// make an array out of SequenceFiles
		var theSequenceFiles = [];

		if (!angular.isArray($scope.xmlGenomeBuild.SequenceFiles)) {
			theSequenceFiles.push($scope.xmlGenomeBuild.SequenceFiles);
		}
		else {
			for (var m=0; m<$scope.xmlGenomeBuild.SequenceFiles.length; m++) {
				theSequenceFiles.push($scope.xmlGenomeBuild.SequenceFiles[m]);
			}
		}

		//console.log("[parseGenomeBuild] theSequenceFiles.length: " + theSequenceFiles.length);

		// setup the labels and data for the segments
		var seqfileLabels = [];

		seqfileLabels.push("Name");						//0
		seqfileLabels.push("Date");					//1
		seqfileLabels.push("Size");					//2

		// remember column headers for the table
		$scope.selectedSeqFileLabels = angular.copy(seqfileLabels);

		// now get the data
		$scope.selectedSeqFiles = [];

		for (var i=0;i<theSequenceFiles.length;i++) {

			var aSeqFile = new Object();
			aSeqFile[seqfileLabels[0]] = "";
			if (angular.isDefined (theSequenceFiles[i].name) ) {
				aSeqFile[seqfileLabels[0]] = theSequenceFiles[i].name;
			}

			aSeqFile[seqfileLabels[1]] = "";
			if (angular.isDefined (theSequenceFiles[i].date) ) {
				aSeqFile[seqfileLabels[1]] = theSequenceFiles[i].date;
			}

			aSeqFile[seqfileLabels[2]] = "";
			if (angular.isDefined (theSequenceFiles[i].size) ) {
				aSeqFile[seqfileLabels[2]] = theSequenceFiles[i].size;
			}

			// add it
			$scope.selectedSeqFiles.push(aSeqFile);
		}; // end of theSequenceFiles.length for

	}; // end of parseGenomeBuild

}]);





