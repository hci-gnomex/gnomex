'use strict';

/* Directives */

var directives = angular.module('directives', [])

.directive('flotChart', [
	function() {
	    return {
	        restrict: 'EA',
	        link: function(scope, element, attr) {
	            scope.$watch(attr.myModel, function(x) {
	                if ((!x) || (!x.data) || x.data.length<2) {
	                    return;
	                }
	                $.plot(element, x.data, x.options);
	            }, true);
	        }
	    };
	}
])
.directive('resize', function ($window) {
    return function (scope, element, attrs) {
        var w = angular.element($window);
        scope.getWindowDimensions = function () {
            return {
                'h': w.height(),
                'w': w.width()
            };
        };
        scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
            scope.style = function () {
                return {
                    'max-height': (newValue.h * attrs.per / 100) + 'px',
                };
            };

        }, true);

        w.bind('resize', function () {
            scope.$apply();
        });
    };
})
.directive('showOnRowHover', function () {
	    return {
	        link: function (scope, element, attrs) {

	            element.closest('tr').bind('mouseenter', function () {
	                element.show();
	            });
	            element.closest('tr').bind('mouseleave', function () {
	                element.hide();

	                var contextmenu = element.find('#contextmenu');
	                contextmenu.click();

	                element.parent().removeClass('open');

	            });

	        }
	    };
});
