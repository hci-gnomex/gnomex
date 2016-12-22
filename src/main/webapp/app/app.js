(function () {
	'use strict';

	angular.module('app', ['ngRoute', 'ui.bootstrap', 'dashboard', 'experiment','navbar', 'analysis','datatrack','topic','report']);


	angular.module('app').config(['$routeProvider',

	  function ($routeProvider) {

		$routeProvider.when('/dashboard', {
		    templateUrl: 'app/dashboard/dashboard.html',
		    controller: 'DashboardController',
		    restrict: 'none'
		});

		$routeProvider.when('/experiment', {
		      templateUrl: 'app/experiment/experiment.html',
		      controller: 'ExperimentController',
		      restrict: 'none'
		});

		$routeProvider.when('/analysis',{
			templateUrl: 'app/analysis/analysis.html',
			controller: 'AnalysisController',
			restrict: 'none'
		});

		$routeProvider.when('/datatrack',{
			templateUrl: 'app/datatrack/datatrack.html',
			controller: 'DataTrackController',
			restrict: 'none'
		});

		$routeProvider.when('/topic',{
			templateUrl: 'app/topic/topic.html',
			controller: 'TopicController',
			restrict: 'none'
		});

		$routeProvider.when('/report',{
			templateUrl: 'app/report/report.html',
			controller: 'ReportController',
			restrict: 'none'
		});

		$routeProvider.otherwise({redirectTo: '/dashboard'});
	}]);

}());








