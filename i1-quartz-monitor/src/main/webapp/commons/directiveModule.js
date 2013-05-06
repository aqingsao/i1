var directiveModule = angular.module('directives', []);
directiveModule.directive('input', function () {
    return {
        require: '?ngModel',
        restrict: 'E',
        link: function (scope, element, attrs, ngModel) {
            if ( attrs.type="datetime-local" && ngModel ) {
                element.bind('change', function () {
                    scope.$apply(function() {
                        ngModel.$setViewValue(element.val());
                    });
                });
            }
        }
    };
});


