var error = angular.module("error",[])

.controller("ErrorController", ['$scope','$modalInstance','$sce','$rootScope','$http','title','message','stackTrace','errorTime',

function($scope, $modal, $sce, $rootScope, $http, title, message, stackTrace, errorTime) {
	$scope.title = title;
	$scope.message = $sce.trustAsHtml(message);
	$scope.stackTrace = stackTrace;
	$scope.errorTime = errorTime;
	$scope.userComments = "";

	$scope.ok = function() {
		$modal.dismiss();
	};

	$scope.sendReport = function() {
		var subject = "Error report from GNomExLite ";
		var user = "guest";
		if ($scope.loggedUser != null) {
			user = $scope.loggedUser.username;
		}

		if ($scope.userComments == "") {
			$scope.userComments = "No user comments";
		}

		var body = "User " + user + " reported an error at " + errorTime + "\n\n" + $scope.userComments +
			"\n\n" + $scope.message + "\n\n" + $scope.stackTrace + "\n\n Peace,\nGNomExLite\n\n" ;
		$http({
			url : "shared/sendMail",
			method : "POST",
			params : {subject: subject, body: body, stackTrace: stackTrace}
		});
		$modal.dismiss();
	};
}])
.controller("userErrorController", ['$scope','$modalInstance','$sce','title','message',
	 function($scope, $modal, $sce, title, message) {
		$scope.title = title;
		$scope.message = $sce.trustAsHtml(message);

		$scope.ok = function() {
			$modal.dismiss();
		};
}]);