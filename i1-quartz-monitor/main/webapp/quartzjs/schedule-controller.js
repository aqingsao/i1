scheduleApp.controller('scheduleController', function scheduleController($scope, $http) {
        $scope.serverShow = false;
        $scope.jobDetailShow = false;
        $scope.triggerShow = false;
        $scope.listenerShow = false;

        $scope.changeServer = function () {
            $scope.serverShow = true;
        };

        $scope.changeJobDetail = function () {
            $scope.jobDetailShow = true;
        };

        $scope.changeTrigger = function () {
            $scope.triggerShow = true;
        };

        $scope.changeListener = function () {
            $scope.listenerShow = true;
        };

    }
);
