scheduleApp.controller('scheduleController', function scheduleController($scope, $http) {

        $scope.units = [
            {"key": 0, "value": "秒"},
            {"key": 1, "value": "分钟"},
            {"key": 2, "value": "小时"},
            {"key": 3, "value": "天"}
        ];

        //初始化
        $scope.jobDetail = new JobDetail();

        $scope.trigger = new Trigger();

        $scope.jobs = [];

        $scope.job = new JobVO();

        $scope.job.jobDetail.addJobDetail($scope.jobDetail);

        $scope.job.triggers.addTrigger($scope.trigger);

        $scope.saveJob = function () {

            $scope.job.jobDetail.addJobData("url", $scope.url);

            for (i = 0; i < $scope.job.triggers.length; i++) {
                var triggerTemp = $scope.job.triggers[i];
                var startDatetime = new Date(triggerTemp.startTime).getTime();
                $scope.job.triggers[i].startTime = startDatetime;

                var endDatetime = new Date(triggerTemp.endTime).getTime();
                $scope.job.triggers[i].endTime = endDatetime;

                $scope.quartz.triggers[i].repeatInterval = getRepeatInterval(triggerTemp.repeatInterval, triggerTemp.repeatIntervalUnit);

            }
            ;

            var tempurl = Path.getUri("api/quartz-jobs/item");
            $http.post(tempurl, $scope.job).success(
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

        function getRepeatInterval(repeatInterval, repeatIntervalUnit) {
            var temp = 0;
            if (repeatIntervalUnit === 0) {
                return repeatInterval * 1000;
            } else if (repeatIntervalUnit === 1) {
                return repeatInterval * 60 * 1000;
            } else if (repeatIntervalUnit === 2) {
                return repeatInterval * 60 * 60 * 1000;
            } else if (repeatIntervalUnit === 3) {
                return repeatInterval * 12 * 60 * 60 * 1000;
            }
            return temp;
        }


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

        $scope.addClo = function () {
            $scope.quartz.addTrigger(new Trigger());
            $scope.triggerInfoList.push(new TriggerInfo());
        };

        $scope.listQuartz();

        $scope.getRepeatIntervalExpress = function (repeatInterval) {
            var temp = 0;
            var tempUnit = "秒"
            if (repeatInterval > 0) {
                if (repeatInterval % (12 * 60 * 60 * 1000) === 0) {
                    temp = repeatInterval / (12 * 60 * 60 * 1000);
                    tempUnit = "天"
                } else if (repeatInterval % (60 * 60 * 1000) === 0) {
                    temp = repeatInterval / (60 * 60 * 1000);
                    tempUnit = "小时"
                } else if (repeatInterval % (60 * 1000) === 0) {
                    temp = repeatInterval / (60 * 1000);
                    tempUnit = "分钟"
                } else {
                    temp = repeatInterval / (1000);
                    tempUnit = "秒"
                }
            }

            return temp + tempUnit;
        } ;

        function getRepeatInterval(repeatInterval, repeatIntervalUnit) {
            var temp = 0;
            if (repeatIntervalUnit === 0) {
                return repeatInterval * 1000;
            } else if (repeatIntervalUnit === 1) {
                return repeatInterval * 60 * 1000;
            } else if (repeatIntervalUnit === 2) {
                return repeatInterval * 60 * 60 * 1000;
            } else if (repeatIntervalUnit === 3) {
                return repeatInterval * 12 * 60 * 60 * 1000;
            }
            return temp;
        }

//        暂停
        $scope.pauseSchedule = function (trigger) {
            var triggerName = trigger.triggerName;
            var triggerGroupName = trigger.triggerGroupName;
            var pauseUrl = Path.getUri("api/quartz-jobs/pause-trigger/" + triggerName + "/" + triggerGroupName);
            $http.get(pauseUrl).success(
                function (data, status, headers, config) {
                    alert("执行暂停成功！");

                }).error(
                function (data, status, headers, config) {
                    alert("暂停失败！");
                }
            );
        };

//        重启trigger
        $scope.resumeTrigger = function (trigger) {
            var resumeUrl = Path.getUri("api/quartz-jobs/resume-trigger/" + trigger.triggerName + "/" + trigger.triggerGroupName);
            $http.get(resumeUrl).success(
                function (data, status, headers, config) {
                    alert("重启执行成功！");
                }
            ).error(
                function (data, status, headers, config) {
                    alert("重启失败！");
                }
            );
        } ;

//        从schedule里面移除trigger
        $scope.removeFromSchedule = function (trigger) {
            var removeUrl = Path.getUri("api/quartz-jobs/delete-trigger/" + trigger.triggerName + "/" + trigger.triggerGroupName);
            $http.get(removeUrl).success(
                function (data, status, headers, config) {
                    alert("移除成功！");
                }
            ).error(
                function (data, status, headers, config) {
                    alert("移除失败！");
                }
            )
        };


    }
)