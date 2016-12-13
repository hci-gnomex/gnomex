'use strict';

/* Services */


var services = angular.module('services', ['ngResource','ui.bootstrap'])


.value('version', '1.0')

.factory('GNomExLiteHttpInterceptor',['$q','$injector',
    function($q,$injector) {

		return {
			'responseError': function(rejection) {

				if (rejection.data == null) {
					return $q.reject(rejection);
			    } else if (rejection.data.errorMessage == null) {
					return $q.reject(rejection);
				} else {
					var $modal = $injector.get('$modal');

					$modal.open({
			    		templateUrl: 'app/common/error.html',
			    		controller: 'ErrorController',
			    		resolve: {
			    			title: function() {
			    				return rejection.data.errorName;
			    			},
			    			message: function() {
			    				return rejection.data.errorMessage;
			    			},
			    			stackTrace: function() {
			    				return rejection.data.errorStackTrace;
			    			},
			    			errorTime: function() {
			    				return rejection.data.errorTime;
			    			}

			    		}
			    	});

			        return $q.reject(rejection);
				}

		     }
		};
}])

.config(function($httpProvider) {
	$httpProvider.interceptors.push('GNomExLiteHttpInterceptor');
})

.factory('DynamicDictionary',['$http',
    function($http) {
		var dict = {};

		dict.loadLabs = function() {
			return $http({
		    	method: 'GET',
		    	url: 'lab/all'
		    });
		};

		dict.loadQueryProjects = function() {
			return $http({
		    	method: 'GET',
		    	url: 'project/getProjectsByVisibility'
		    });
		};


		dict.loadSamplePrepsBySampleType = function(idSampleType) {
			return $http({
				method: 'GET',
				url: 'shared/getSamplePrepsBySampleType',
				params : {idSampleType: idSampleType},
			});
		};

		dict.loadSampleSources = function() {
			return $http({
				method: 'GET',
				url: 'shared/getAllSampleSources',
			});
		};

		dict.loadSampleConditions = function() {
			return $http({
				method: 'GET',
				url: 'shared/getAllSampleConditions',
			});
		};

		dict.loadSamplePreps = function() {
			return $http({
				method: 'GET',
				url: 'shared/getAllSamplePreps',
			});
		};

		dict.isAuthenticated = function() {
			return $http({
				method: 'GET',
				url: 'security/auth',
			});
		};

		dict.loadQueryLabs = function() {
			return $http({
				method: 'GET',
				url: 'lab/getQueryLabs',
			});
		};

		dict.loadQueryAnalyses = function() {
			return $http({
				method: 'GET',
				url: 'project/getAllAnalyses',
			});
		};

		dict.loadOrganismBuilds = function() {
			return $http({
				method: 'GET',
				url: 'shared/getAllBuilds',
			});
		};

		dict.loadOrganisms = function() {
			return $http({
				method: 'GET',
				url: 'shared/getAllOrganisms',
			});
		};

		return dict;

}]);


