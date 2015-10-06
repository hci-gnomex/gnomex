/**
 * NavBarController -- Handles the navbar and creates the security advisor, gets the propertylist, dictionaries, and labs for common use
 *
 */

var navbar = angular.module('navbar', ['ui.bootstrap','treeControl','treeGrid', 'filters', 'services', 'directives','chosen','ngProgress','dialogs.main','error']);

angular.module('navbar')
  .factory('SecurityAdvisorService', function($http) {
    return {
      create: function() {
         return $http.get('/gnomexlite/CreateSecurityAdvisor.gx');
      }
    }
  })
    .factory('PropertyListService', function($http) {
      return {
        getPL: function() {
           return $http.get('/gnomexlite/GetPropertyList.gx');
        }
      }
    })
  .factory('ManageDictionariesService', function($http) {
    return {
      initDict: function() {
         return $http.get('/gnomexlite/ManageDictionaries.gx?action=load');
      }
    }
  })
  .factory('LabList', function($http) {
    return {
      getLabs: function() {
         return $http.get('/gnomexlite/GetLabList.gx');
      }
    }
  })


.controller("NavbarController",['$modal','$scope','$http','$rootScope','$location','$interval','$route','DynamicDictionary','ngProgress','dialogs','SecurityAdvisorService','PropertyListService','ManageDictionariesService','LabList',
	function($modal,$scope,$http,$rootScope,$location,$interval,$route,DynamicDictionary,ngProgress,dialogs,SecurityAdvisorService,PropertyListService,ManageDictionariesService,LabList) {

$rootScope.securityAdvisor = "";
$rootScope.propertyList = "";
$rootScope.manageDictionaries = "";
$rootScope.propertyOptions = [];
$rootScope.labs = [];
$rootScope.okToPickLab = true;
$rootScope.okToFind = true;


	    $rootScope.loggedUser = null;
	    $rootScope.lastLocation = "dashboard";
		$rootScope.admin = false;
		$rootScope.helpMessage = "";

		$rootScope.whatToLookup = "";
		$rootScope.whatAnalysisToLookup = "";
		$rootScope.whatDataTrackToLookup = "";
		$rootScope.whatTopicToLookup = "";

		$scope.whatToLookup = "";

// start the world
activate();

	function activate() {
		createSecurityAdvisor();

	};

    function getPropertyList() {
       var promise = PropertyListService.getPL();

       promise.then(
          function(payload) {
              $rootScope.propertyList = payload.data;
              buildPropertyOptions();
          },
          function(errorPayload) {
              console.log('[navbarcontroller] failure getting Property List' + errorPayload);
          });
     };

     function getDictionaries() {
       var promise = ManageDictionariesService.initDict();

       promise.then(
          function(payload) {
              $rootScope.manageDictionaries = payload.data;
              getPropertyList();
          },
          function(errorPayload) {
              console.log('[navbarcontroller] failure manage Dictionaries' + errorPayload);
          });
     };

     function getLabs() {
       var promise = LabList.getLabs();

       promise.then(
          function(payload) {
              $rootScope.labs = payload.data;
              $rootScope.okToPickLab = false;
              $scope.$broadcast('okToPickLab', { message: false });
              $rootScope.$broadcast('okToPickLab', { message: false });              
              console.log ("[navbarcontroller] after oktopicklab broadcast");
          },
          function(errorPayload) {
              console.log('[navbarcontroller] failure manage Dictionaries' + errorPayload);
          });
     };

     function createSecurityAdvisor() {
       var promise = SecurityAdvisorService.create();

       promise.then(
          function(payload) {
              $rootScope.securityAdvisor = payload.data;
              getLabs();
              getDictionaries();
          },
          function(errorPayload) {
              console.log('failure creating security advisor' + errorPayload);
          });
     };

	function buildPropertyOptions() {

		$rootScope.propertyOptions = new Object();

		for (var i=0; i<$rootScope.propertyList.length; i++) {
			if ($rootScope.propertyList[i].options.length == 0) {
				continue;
			}

			var propertyMap = new Object();

			for (var j=0; j<$rootScope.propertyList[i].options.length; j++) {
				propertyMap[$rootScope.propertyList[i].options[j].value] = $rootScope.propertyList[i].options[j].option;
			}; // end of for

			$rootScope.propertyOptions[$rootScope.propertyList[i].value] = angular.copy(propertyMap);
		}; // end of for

	    //console.log("[buildPropertyOptions] $rootScope.propertyOptions.length: " + $rootScope.propertyOptions.length);
	    //for (var propertyName in $rootScope.propertyOptions) {
		//	console.log("[buildPropertyOptions] $rootScope.propertyOptions propertyName: " + propertyName);
		//};

		$rootScope.okToFind = false;
		$scope.$broadcast('okToFind', { message: false });
		$rootScope.$broadcast('okToFind', { message: false });
		console.log ("[navbarcontroller] after okToFind broadcast");
		//console.log("[navbarcontroller] ** leaving buildPropertyOptions **");

	}; // end of buildPropertyOptions

	function lookupType (whatToLookup) {
		var type = "U";
		if (whatToLookup == null || whatToLookup == "") {
			return type;
		}

		// assume it's an experiment
		type = "E";

		// does it start with an "A"
		var firstChar = whatToLookup.charAt(0);
		if (firstChar == "A") {
			type = "A";
		}
		else if (firstChar == "D") {
			type = "D";
		}
		else if (firstChar == "T") {
			type = "T";
		}

		return type;
	}; // end of lookupType


		$scope.lookup = function(msg) {
			var msgCleaned = msg.trim().toUpperCase();
			console.log("[lookup] $scope.whatToLookup: " + $scope.whatToLookup + "msg: " + msg + " msgCleaned: " + msgCleaned);

			var whatType = lookupType (msgCleaned);
			console.log("[lookup] whatType: " + whatType);

			if (whatType == "E") {
				// broadcast what we're looking up
				$scope.$broadcast('lookupExperiment', { message: msgCleaned });
				$rootScope.$broadcast('lookupExperiment', { message: msgCleaned });
				$scope.whatToLookup = msgCleaned;
				$rootScope.whatToLookup = msgCleaned;

				// go to experiment
				$location.path("/experiment");
			} else if (whatType == "A") {
				// broadcast what we're looking up
				$scope.$broadcast('lookupAnalysis', { message: msgCleaned });
				$rootScope.$broadcast('lookupAnalysis', { message: msgCleaned });
				$rootScope.whatAnalysisToLookup = msgCleaned;

				// go to analysis
				$location.path("/analysis");
			} else if (whatType == "D") {
				// broadcast what we're looking up
				$scope.$broadcast('lookupDataTrack', { message: msgCleaned });
				$rootScope.$broadcast('lookupDataTrack', { message: msgCleaned });
				$rootScope.whatDataTrackToLookup = msgCleaned;

				// go to datatrack
				$location.path("/datatrack");
			} else if (whatType == "T") {
				// broadcast what we're looking up
				$scope.$broadcast('lookupTopic', { message: msgCleaned });
				$rootScope.$broadcast('lookupTopic', { message: msgCleaned });
				$rootScope.whatTopicToLookup = msgCleaned;

				// go to topic
				$location.path("/topic");
			}

		}; // end of lookup


		$rootScope.$on('$routeChangeStart',function(event, next, prev) {
			var url = "/dashboard";
			if (prev != undefined) {
				url = prev.originalPath;
			}

		});

		$scope.displayHelp = function() {
			$modal.open({
	    		templateUrl: 'app/common/userError.html',
	    		controller: 'userErrorController',
	    		resolve: {
	    			title: function() {
	    				return "Help";
	    			},
	    			message: function() {
	    				return $rootScope.helpMessage;
	    			}
	    		}
	    	});
		};





}]);