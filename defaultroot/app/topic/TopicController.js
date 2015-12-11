'use strict';

/**
 * TopicController
 * @constructor
 */

var topic    = angular.module('topic', ['ui.bootstrap','treeControl','treeGrid', 'filters', 'services', 'directives','chosen','ngProgress','dialogs.main','error']);
console.log("In TopicController.js");

angular.module("topic")
  .factory('TopicListService', function($http) {
    return {
      getTopicList: function(whatever) {
         return $http.get('/gnomexlite/GetTopicList.gx');
      }
    }
  })

.controller("TopicController", [
'$scope', '$http', '$modal','$rootScope','$q','ngProgress','dialogs','TopicListService',
function($scope, $http, $modal, $rootScope, $q, ngProgress, dialogs, TopicListService) {
	/**********************
	 * Initialization!
	 *********************/
console.log("In TopicController.js 2");
$scope.topicList = "";
$scope.okToFind = true;						// comes from $rootScope
$scope.okToPickLab = true;					// comes from $rootScope
$scope.whatToLookup = $rootScope.whatTopicToLookup;
$scope.showColumnForm = true;
$scope.showProgressBar = false;

$scope.searchFilter = {selectAll: false, group: "", owner: "", lookup: ""};

$scope.labs = [];							// comes from $rootScope
$scope.topicTree = [];
$scope.expandedTopicTree = [];

$scope.selectedDescription = {};
$scope.topicSelected = {};

$scope.topicEditMode = false;
$scope.labSelected = "";

$scope.topicList = 'app/topic/topicFolderDetail.html';
$scope.topicDetail = 'app/topic/topicDetail.html';
$scope.topicFolderDetail = 'app/topic/topicFolderDetail.html';

$scope.topicForm = $scope.topicList;

$scope.topicSelected = [];

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
	$scope.showProgressBar = true;

    // do they want all of them?
    var whatToGet = "?allTopic=Y";
    if ($scope.searchFilter.selectAll) {
		whatToGet = "?allTopic=Y";
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

	//console.log("[find] *** calling getTopicList, whatToGet: " + whatToGet);
	getTopicList(whatToGet);
};


$scope.lookUp = function() {
	//console.log("[topic lookUp] current searchFilter, selectAll: " + $scope.searchFilter.selectAll + " group: " + $scope.searchFilter.group + " lookup: " + $scope.searchFilter.lookup);

	// get the topic, if it exists, setup the topicTree for the lab and display the topic
	var tid = mapLookup($scope.searchFilter.lookup);
	lookupTopic(tid);
};

 $scope.$on('lookupTopic', function (event, args) {
	 $scope.searchFilter.lookup = args.message;
	 var tid = mapLookup($scope.searchFilter.lookup);
	 console.log("[topic $on lookupTopic event] $scope.searchFilter.lookup " + $scope.searchFilter.lookup + " tid: " + tid);

	 // do the work
	 lookupTopic(tid);
 });

 $scope.$on('okToPickLab', function (event, args) {
	 // we got all the lab information
	 $scope.labs = angular.copy($rootScope.labs);
	 $scope.okToPickLab = false;
	 console.log("[TopicController] $on okToPickLab event");
 });

 $scope.$on('okToFind', function (event, args) {
	 // we got all the lab information
	 $scope.okToFind = false;
	 console.log("[TopicController] $on okToFind event");
 });

 
 	/*********************
 	 * Watchers
 	 ********************/
     $scope.$watch('whatToLookup',function() {
		console.log("[topic whatToLookup watcher] $scope.whatToLookup: " + $scope.whatToLookup);
     	if ($scope.whatToLookup != null && $scope.whatToLookup != "") {
			$scope.searchFilter.lookup = $scope.whatToLookup;
			var tid = mapLookup($scope.searchFilter.lookup);
			lookupTopic(tid);
     	}
     });

     $scope.$watch('okToPickLab',function() {
    		//console.log("[TopicController] okToPickLab watcher $scope.okToPickLab: " + $scope.okToPickLab);
         	if (!$scope.okToPickLab) {
         		$scope.labs = angular.copy($rootScope.labs);
         	}
         });
        
        $scope.$watch('okToFind',function() {
     		//console.log("[TopicController] okToFind watcher $scope.okToFind: " + $scope.okToFind);
          });
   

     $scope.showSelected = function(sel) {
		//console.log("In showSelected " + sel.type + " " + sel.label + " " + $scope.topicForm);
		$scope.showColumnForm = true;

         $scope.selectedNode = sel;
         if (sel.type == "F") {
		 	$scope.folderSelected = angular.copy(sel);
		 	$scope.topicForm = $scope.topicFolderDetail;
		}
         if (sel.type == "T") {
		 	$scope.topicSelected = angular.copy(sel);
		 	$scope.topicForm = $scope.topicDetail;
		}
		if (sel.type == "E") {
			$scope.experimentSelected = angular.copy(sel);
			$scope.topicForm = $scope.topicFolderDetail;
		}
		if (sel.type == "A") {
			$scope.analysisSelected = angular.copy(sel);
			$scope.topicForm = $scope.topicFolderDetail;
		}
		//console.log("In showSelected, form: " + $scope.topicForm);
     };


	function activate() {

		// get what we need from the $rootScope
		$scope.labs = angular.copy($rootScope.labs);
		console.log("[TopicController activate] $scope.labs.length: " + $scope.labs.length);
		$scope.okToPickLab = angular.copy($rootScope.okToPickLab);
		console.log("[TopicController activate] $scope.okToPickLab: " + $scope.okToPickLab);
		$scope.okToFind = angular.copy($rootScope.okToFind);
		console.log("[TopicController activate] $scope.okToFind: " + $scope.okToFind);
		$scope.showProgressBar = angular.copy($rootScope.showProgressBar);
	};

     function getTopicList(whatToGet) {
       var promise = TopicListService.getTopicList(whatToGet);

       promise.then(
          function(payload) {
              $scope.topicList = payload.data;

              // setup the topic tree
              buildTopicTree();
              $scope.showProgressBar = false;
          },
          function(errorPayload) {
              console.log('failure getTopicList' + errorPayload);
          });
     };

     function lookupTopic(tid) {
		 console.log("[lookupTopic] ** before getTopicList ** tid: " + tid);

		 // get the topic list (because that's all we've got, there is no getTopic)
         var whatToGet = "?allDataTrack=Y";
         var promise = TopicListService.getTopicList(whatToGet);

       	 promise.then(
          function(payload) {
              $scope.topicList = payload.data;

			  // don't display the default form for topic
			  $scope.showColumnForm = false;

              // setup the topic tree
              buildTopicTree();
          },
          function(errorPayload) {
              console.log('failure (in lookupTopic) getTopicList' + errorPayload);
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


	function mapLookup(requestNumber) {
		var tid = requestNumber;
		if (requestNumber == null || requestNumber == "") {
			//console.log("[topic mapLookup] 1 returning tid: " + tid);
			return tid;
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
			//console.log("[topic mapLookup] 2 returning tid: " + tid);
			return tid;		// nope
		}

		if (epos == -1) {
			tid = requestNumber.substring(spos);
		}
		else {
			tid = requestNumber.substring(spos,epos);
		}

		//console.log("[topic mapLookup] 3 returning tid: " + tid + " requestNumber: " + requestNumber + " spos: " + spos + " epos: " + epos);
		return tid;

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


     function buildTopicTree () {

		//console.log("[buildTopicTree] $scope.topicList.length " + $scope.topicList.length);
		$scope.topicTree = [];

		var theTop = {type: "F", label: "Topics", children: []};

		// make an array out of Topics
		var theTopics = [];

		if (angular.isDefined($scope.topicList.Folder.Topic) ) {
			if (!angular.isArray(theTopics)) {
				theTopics.push($scope.topicList.Folder.Topic);
			}
			else {
				for (var k=0; k<$scope.topicList.Folder.Topic.length; k++) {
					theTopics.push($scope.topicList.Folder.Topic[k]);
				}
			}
		}

		//console.log("[buildTopicTree] Top Level Topic -- theTopics.length: " + theTopics.length);

		// process the Topics
		for (var l=0; l<theTopics.length; l++) {
			//console.log("[buildTopicTree] Processing theTopics[" + l + "]: " + theTopics[l].label);

			var aTopic = processTopic(theTopics[l]);

			// add it as a child
			$scope.topicTree.push(aTopic);
		} // end of theTopics for


		// make the tree be expanded by default
		for (var i = 0; i<$scope.topicTree.length; i++) {
			$scope.expandedTopicTree.push($scope.topicTree[i]);

			for (var j = 0;j<$scope.topicTree[i].children.length;j++) {
				$scope.expandedTopicTree.push($scope.topicTree[i].children[j]);
			}
		}

	  if (!$scope.showColumnForm) {
		  // we got here doing a Lookup, pretend someone clicked on the topic of interest...
		  var foundIT = false;
		  var tid = mapLookup($scope.searchFilter.lookup);

		  for (var i = 0; i<$scope.topicTree.length; i++) {

			for (var j = 0;j<$scope.topicTree[i].children.length;j++) {
				var sel = findATopic ($scope.topicTree[i].children[j],tid);

	         	if (sel.type == "T" && sel.etype == tid) {
		 			$scope.topicSelected = angular.copy(sel);
		 			$scope.topicForm = $scope.topicDetail;

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

	 }; // end of buildTopicTree

	 // walk the topicTree until we find the topic of interest
	 function findATopic (node,tid) {

		 // just in case...
		 // is it a topic? and the one we want?
		 if (node.type == "T" && node.etype == tid) {
			// yes, give it back
			return node;
		 }

		 for (var i=0;i<=node.children.length;i++) {
			 var child = angular.copy(node.children[i]);

			 // is it a topic? and the one we want?
			 if (child.type == "T" && child.etype == tid) {
				 // yes, give it back
				 return child;
			 }

			 // if no children we are done with this child
			 if (child.children.length == 0) {
				 continue;
			 }

			 // down we go
			 var result = findATopic (child,tid);

			 if (result == null) {
				 // nothing down there of interest
				 continue;
			 }

			 // is it a topic? and the one we want?
			 if (result.type == "T" && result.etype == tid) {
				 // yes, give it back
				 return result;
			 }
		 } // end of for i

		 // walked all the way to bottom of this branch and found nothing
		 return null;

	 }; // end of findATopic


	 // recursively call ourselves until all the nested topics have been dealt with
     function processTopic (aTopic) {
		//console.log("[processTopic] *** starting *** aTopic.label " + aTopic.label);

		// set up the Topic
		var theTopic = {type: "T", label: "", etype: "", name: "", lab: "", owner: "", description: "", visibility: "", createdBy: "", createDate: "", children: [] };

		var labelPrefix = "T" + aTopic.idTopic + " - ";
		theTopic.label = "T" + aTopic.idTopic + " - " + aTopic.label;
		theTopic.etype = aTopic.idTopic;
		theTopic.name = aTopic.name;
		theTopic.lab = aTopic.lab;
		theTopic.owner = "";
		theTopic.description = parseText(aTopic.description);
		theTopic.visibility = mapVisibility(aTopic.codeVisibility);
		theTopic.createdBy = aTopic.createdBy;
		theTopic.createDate = aTopic.createDate;

		// shorten it
		if (theTopic.label.length > 36) {
			theTopic.label = theTopic.label.substr(0, 36);
		}

		// is there a nested Topic?
		if (angular.isDefined(aTopic.Topic) ) {

			// make sure it's an array
			var nestedTopics = [];

			if (!angular.isArray(aTopic.Topic)) {
				nestedTopics.push(aTopic.Topic);
			}
			else {
				for (var k=0; k<aTopic.Topic.length; k++) {
					nestedTopics.push(aTopic.Topic[k]);
				}
			}

			//console.log("[processTopic] in topic: " + aTopic.label + " nestedTopics.length: " + nestedTopics.length);

			// process the nestedTopics
			for (var l=0; l<nestedTopics.length; l++) {
				//console.log("[buildTopicTree] Processing nestedTopics[" + l + "]: " + nestedTopics[l].label);

				var theNestedTopic = processTopic(nestedTopics[l]);

				// add it as a child
				theTopic.children.push(theNestedTopic);

			} // end of for nestedTopics

			// if we get here we have already dealt with the experiments / analyses in some nested topic...
			// so we're done
			return theTopic;

		} // end of if aTopic has a Topic


		// Does this topic contain a category (i.e., an experiment and/or analysis)?
		if (angular.isDefined(aTopic.Category) ) {
			// we finally got to the end of nested topics...

			// make an array out of Category
			var theCategorys = [];

			if (!angular.isArray(aTopic.Category)) {
				theCategorys.push(aTopic.Category);
			}
			else {
				for (var m=0; m<aTopic.Category.length; m++) {
					theCategorys.push(aTopic.Category[m]);
				}
			}

			//console.log("[processTopic] inside topic: " + aTopic.label + " theCategorys.length: " + theCategorys.length);

			// process the theCategorys
			for (var m=0; m<theCategorys.length; m++) {
				//console.log("[processTopic] Processing theCategorys[" + m + "]: " + theCategorys[m].label);

				var theCategory = {type: "F", label: "", etype: "", children: [] };
				theCategory.label = labelPrefix + theCategorys[m].label;

				// shorten it
				if (theCategory.label.length > 36) {
					theCategory.label = theCategory.label.substr(0, 36);
				}

				// set up theCategory etype
				theCategory.etype = "F";

				// Does this category contain a request (i.e., an experiment)?
				if (angular.isDefined(theCategorys[m].Request) ) {
					// we finally got to the end of nested topics...

					// make an array out of Request
					var theRequests = [];

					if (!angular.isArray(theCategorys[m].Request)) {
						theRequests.push(theCategorys[m].Request);
					}
					else {
						for (var n=0; n<theCategorys[m].Request.length; n++) {
							theRequests.push(theCategorys[m].Request[n]);
						}
					}

					//console.log("[processTopic] inside category: " + theCategory.label + " theRequests.length: " + theRequests.length);

					// process the theRequests
					for (var n=0; n<theRequests.length; n++) {
						//console.log("[processTopic] Processing theRequests[" + n + "]: " + theRequests[n].label);

						var theRequest = {type: "E", label: "", etype: "", idRequest: "", number: "", name: "", labName: "", children: [] };
						theRequest.label = theRequests[n].requestNumber + " - " + theRequests[n].name;

						// shorten it
						if (theRequest.label.length > 31) {
							theRequest.label = theRequest.label.substr(0, 31);
						}

						// set up theRequest type
						theRequest.etype = "E";

						theRequest.idRequest = theRequests[n].idRequest;
						theRequest.number = theRequests[n].number;
						theRequest.name = theRequests[n].name;
						theRequest.labName = theRequests[n].labName;

						// add it as a child of the Category
						theCategory.children.push(theRequest);
					} // end of for theRequests

					// add the category to the topic
					theTopic.children.push(theCategory);

				} // end of if theCateory.Request is defined


				// Does this category contain an analysis?
				if (angular.isDefined(theCategorys[m].Analysis) ) {

					// make an array out of Analysis
					var theAnalyses = [];

					if (!angular.isArray(theCategorys[m].Analysis)) {
						theAnalyses.push(theCategorys[m].Analysis);
					}
					else {
						for (var n=0; n<theCategorys[m].Analysis.length; n++) {
							theAnalyses.push(theCategorys[m].Analysis[n]);
						}
					}

					//console.log("[processTopic] inside category: " + theCategory.label + " theAnalyses.length: " + theAnalyses.length);

					// process the theAnalyses
					for (var n=0; n<theAnalyses.length; n++) {
						//console.log("[processTopic] Processing theAnalyses[" + n + "]: " + theAnalyses[n].label);

						var theAnalysis = {type: "A", label: "", etype: "", idAnalysis: "", number: "", name: "", labName: "", children: [] };
						theAnalysis.label = theAnalyses[n].number + " - " + theAnalyses[n].name;

						// shorten it
						if (theAnalysis.label.length > 31) {
							theAnalysis.label = theAnalysis.label.substr(0, 31);
						}

						// set up theAnalysis type
						theAnalysis.etype = "A";

						theAnalysis.idAnalysis = theAnalyses[n].idAnalysis;
						theAnalysis.number = theAnalyses[n].number;
						theAnalysis.name = theAnalyses[n].name;
						theAnalysis.labName = theAnalyses[n].labName;

						// add it as a child of the Category
						theCategory.children.push(theAnalysis);
					} // end of for theAnalyses

					// add the category to the topic
					theTopic.children.push(theCategory);

				} // end of if theCateory.Analysis is defined

			} // end of for theCategorys

		} // end of aTopic.Category is defined

		// return the topic object we build
		return theTopic;

	}; // end of processTopic

}]);





