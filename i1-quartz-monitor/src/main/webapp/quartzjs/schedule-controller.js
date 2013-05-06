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
        $scope.quartzList = [];

        $scope.quartz = new Quartz();

        $scope.quartz.addTrigger(new Trigger());

        $scope.saveQuartz = function () {

            $scope.quartz.addJobData("url", $scope.url);

            var url = Path.getUri("api/quartz-jobs/item");
            $http.post(url, $scope.quartz).success(
                function (data, status, headers, config) {
                    alert("保存成功！");
                    $scope.quartzList = [];
                    for (var j = 0; j < data.length; j++) {
                        var tempQuartz = new Quartz();
                        $scope.quartzList.push(tempQuartz.copyQuartzVO(data[j]));
                    }
                }).error(
                function (data, status, headers, config) {
                    alert("保存失败！");
                }
            );
        };

        $scope.listQuartz = function () {

            var url = Path.getUri("api/quartz-jobs/items");
            $http.get(url).success(
                function (data, status, headers, config) {
                    $scope.quartzList = [];
                    for (var j = 0; j < data.length; j++) {
                        var tempQuartz = new Quartz();
                        $scope.quartzList.push(tempQuartz.copyQuartzVO(data[j]));
                    }
                }).error(
                function (data, status, headers, config) {

                }
            );
        };

        $scope.addClo = function(){
            $scope.quartz.addTrigger(new Trigger());
        }

        $scope.listQuartz();

    }
)
;
