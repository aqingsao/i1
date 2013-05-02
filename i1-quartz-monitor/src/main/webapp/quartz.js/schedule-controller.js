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


        $scope.setTrigger = function () {
            $scope.trigger.triggerName = $scope.triggerName;
            $scope.trigger.triggerGroupName = $scope.triggerGroupName;
            $scope.trigger.startTime = $scope.startTime;
            $scope.trigger.endTime = $scope.endTime;
            $scope.trigger.triggerState = $scope.triggerState;
            $scope.trigger.repeatCount = $scope.repeatCount;
            $scope.trigger.repeatInterval = $scope.repeatInterval;
        };

        $scope.setJobdetail = function () {
            $scope.jobdetail.jobName = $scope.jobName;
            $scope.jobdetail.jobGroupName = $scope.jobGroupName;
            $scope.jobdetail.jobClass = $scope.jobClass;
        };

        $scope.savequartz = function () {
            var job = new Job();

            var trigger = new Trigger();
            $scope.trigger.triggerName = $scope.triggerName;
            $scope.trigger.triggerGroupName = $scope.triggerGroupName;
            $scope.trigger.startTime = $scope.startTime;
            $scope.trigger.endTime = $scope.endTime;
            $scope.trigger.triggerState = $scope.triggerState;
            $scope.trigger.repeatCount = $scope.repeatCount;
            $scope.trigger.repeatInterval = $scope.repeatInterval;

            var jobdetail = new JobDetail();
            $scope.jobdetail.jobName = $scope.jobName;
            $scope.jobdetail.jobGroupName = $scope.jobGroupName;
            $scope.jobdetail.jobClass = $scope.jobClass;

            job.addTrigger(trigger);

            job.addJobDetail(jobdetail);

            var url = Path.getUri("api/jobs/schedule-json");
            $http.post(url, job).success(
                function (data, status, headers, config) {
                    alert("保存成功");
                }).error(
                function (data, status, headers, config) {
                    alert("保存成功");
                }
            );
        } ;

        $scope.currentJob = [];

        var jobUrl = Path.getUri("api/jobs/all-exam-cause-dict");
        $http.get(jobUrl).success(
            function (data) {
                $scope.currentJob = angular.copy(data);
            }
        );



    }
)
;
