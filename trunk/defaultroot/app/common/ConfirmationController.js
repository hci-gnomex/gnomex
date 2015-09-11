'use strict';

/**
 * Confirmation
 * @constructor
 */
var confirmation = angular.module("confirmation",[])

.controller("ConfirmationController", ['$scope','$modalInstance','data',
                                                      
function($scope, $modal, data) {
	$scope.data = data;
	
	$scope.confirm = function() {
		$modal.close();
	};
	
	$scope.cancel = function() {
		$modal.dismiss();
	};
}]);