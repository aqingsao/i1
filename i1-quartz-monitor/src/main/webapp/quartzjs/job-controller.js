scheduleApp.controller('scheduleController', function scheduleController($scope, $http) {

        var TriggerINfo = new Trigger();

        $scope.units = [
            {"key": 0, "value": "秒"},
            {"key": 1, "value": "分钟"},
            {"key": 2, "value": "小时"},
            {"key": 3, "value": "天"}
        ];

         //初始化
        $scope.jobDetail = new JobDetail();
        $scope.trigger = new Trigger();

        $scope.job = new JobVO();
        $scope.job.jobDetail.addJobDetail($scope.jobDetail);
        $scope.job.triggers.addTrigger($scope.trigger);



    }
)