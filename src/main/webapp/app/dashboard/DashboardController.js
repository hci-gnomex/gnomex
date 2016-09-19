'use strict';

/**
 * DashboardController
 * @constructor
 */
var dashboard = angular.module('dashboard', ['services','error']);
console.log("In DashboardController.js");

angular.module('dashboard')

.controller('DashboardController',
[ '$rootScope','$scope','$http',

function($rootScope, $scope, $http) {

    $scope.rnaseq = {};
    $scope.rnaseq.data = [];

    $scope.chipseq = {};
    $scope.chipseq.data = [];

    $scope.bisseq = {};
    $scope.bisseq.data = [];

    $rootScope.helpMessage = "<p>Placeholder for dashboard help.</p>";
}
]);

