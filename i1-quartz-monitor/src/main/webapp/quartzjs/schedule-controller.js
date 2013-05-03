scheduleApp.controller('scheduleController', function scheduleController($scope, $http) {

        $scope.serverShow = false;
        $scope.jobDetailShow = true;
        $scope.triggerShow = false;
        $scope.listenerShow = false;

        $scope.changeContent = function () {
            $scope.serverShow = false;
            $scope.jobDetailShow = false;
            $scope.triggerShow = false;
            $scope.listenerShow = false;
            $scope.cronpressShow = false;
        };

//        初始化
        $scope.trigger = new Trigger();
        $scope.quartz = new Quartz();
        $scope.quartz.triggerList.push($scope.trigger);

        $scope.savequartz = function () {

//            $scope.quartz = new Quartz();
            $scope.quartz.jobName = $scope.jobName;
            $scope.quartz.jobGroupname = $scope.jobGroupName;
            $scope.quartz.jobClass = $scope.jobClass;
            $scope.quartz.description = $scope.description;
            $scope.quartz.url = $scope.url;
//            $scope.quartz.triggerList = $scope.tempTriggerList;

            var url = "http://localhost:8051/schedule/api/quartz-jobs/item";
            $http.post(url, $scope.quartz).success(
                function (data, status, headers, config) {
                    alert("保存成功");
                }).error(
                function (data, status, headers, config) {
                    alert("保存成功");
                }
            );
        };

        $scope.addClo = function(){
             $scope.trigger = new Trigger();
            $scope.quartz.triggerList.push($scope.trigger);
        }



    }
)
;
