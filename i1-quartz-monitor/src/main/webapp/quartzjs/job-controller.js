jobApp.controller('jobController', function jobController($scope, $http) {
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

        $scope.units = [
            {"key": 0, "value": "秒"},
            {"key": 1, "value": "分钟"},
            {"key": 2, "value": "小时"},
            {"key": 3, "value": "天"}
        ];

        var TriggerInfo = function () {
            startTime = "";
            endTime = "";
            repeatInterval = 0;
            repeatIntervalUnit = 0;
        };

        $scope.triggerInfoList = [];
        $scope.triggerInfoList.push(new TriggerInfo());

        $scope.jobs = [];
        //初始化
        $scope.jobVO = new JobVO();
        $scope.jobVO.addJobDetail(new JobDetailVO());
        $scope.jobVO.addTrigger(new TriggerVO());

        $scope.saveJob = function () {

            $scope.jobVO.detail.addJobData("url", $scope.url);

            for (i = 0; i < $scope.triggerInfoList.length; i++) {

                var triggerTemp = $scope.triggerInfoList[i];
                var startDatetime = new Date(triggerTemp.startTime).getTime();
                $scope.jobVO.triggers[i].startTime = startDatetime;

                var endDatetime = new Date(triggerTemp.endTime).getTime();
                $scope.jobVO.triggers[i].endTime = endDatetime;

                $scope.jobVO.triggers[i].repeatInterval = getRepeatInterval(triggerTemp.repeatInterval, triggerTemp.repeatIntervalUnit);

            }
            ;

            var tempurl = Path.getUri("api/quartz-jobs/item");
            console.debug("----------------------");
            console.debug($scope.jobVO);
            $http.post(tempurl, $scope.jobVO).success(
                function (data, status, headers, config) {

                    alert("保存成功！");
                    $scope.jobs = [];
                    for (var j = 0; j < data.length; j++) {
                        var tempJob = new JobVO();
                        $scope.jobs.push(tempJob.copyJob(data[j]));
                    }
                    $scope.listQuartz();

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
                    $scope.jobs = [];
                    for (var j = 0; j < data.length; j++) {
                        var tempJob = new JobVO();
                        $scope.jobs.push(tempJob.copyJob(data[j]));
                    }
                }).error(
                function (data, status, headers, config) {

                }
            );
        };

        $scope.addTrigger = function () {
            $scope.jobVO.addTrigger(new TriggerVO());
            $scope.triggerInfoList.push(new TriggerInfo());
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
        };

        $scope.listQuartz();


        //暂停
        $scope.pauseSchedule = function (trigger) {
            var triggerName = trigger.triggerName;
            var triggerGroupName = trigger.triggerGroupName;
            var pauseUrl = Path.getUri("api/quartz-jobs/pause-trigger/" + triggerName + "/" + triggerGroupName);
            $http.get(pauseUrl).success(
                function (data, status, headers, config) {
                    alert("执行暂停成功！");
                    $scope.listQuartz();
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
                    $scope.listQuartz();
                }
            ).error(
                function (data, status, headers, config) {
                    alert("重启失败！");
                }
            );
        };

//        从schedule里面移除trigger
        $scope.removeFromSchedule = function (trigger) {
            var removeUrl = Path.getUri("api/quartz-jobs/delete-trigger/" + trigger.triggerName + "/" + trigger.triggerGroupName);
            $http.get(removeUrl).success(
                function (data, status, headers, config) {
                    alert("移除成功！");
                    $scope.listQuartz();
                }
            ).error(
                function (data, status, headers, config) {
                    alert("移除失败！");
                }
            )
        };

        //查看qrtzhistory信息
//        $scope.value = "";
//        $scope.detail = function(){
//            $scope.value ='.quartz-content';
//            $scope.listQuartzHistory();

//        }
        $scope.qrtzHistoryList = [];

        $scope.getHistoryInfo = function (trigger) {
            $scope.triggerName = trigger.triggerName;
            $scope.triggerGroupName = trigger.triggerGroupName;
            $scope.listQuartzHistory();
        };

        $scope.listQuartzHistory = function () {
            var url = Path.getUri("api/quartz-jobs/exception-list/" + $scope.triggerName + "/" + $scope.triggerGroupName);
            $http.get(url).success(
                function (data, status, headers, config) {
//                      alert("You are success!")
                    $scope.qrtzHistoryList = angular.copy(data);


                    console.debug($scope.qrtzHistoryList);
                }).error(
                function (data, status, headers, config) {

                }
            );

        };


        $scope.filterOptions = {
            filterText: "",
            useExternalFilter: true
        };
        $scope.pagingOptions = {
            pageSizes: [10, 250, 500, 1000],
            pageSize: 250,
            totalServerItems: 0,
            currentPage: 1
        };
        $scope.setPagingData = function (data, page, pageSize) {
            var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
            $scope.qrtzHistoryList = pagedData;
            $scope.pagingOptions.totalServerItems = data.length;
            if (!$scope.$$phase) {
                $scope.$apply();
            }
        };
        $scope.getPagedDataAsync = function (pageSize, page, searchText) {
            setTimeout(function () {
                var data;
                if (searchText) {
                    var ft = searchText.toLowerCase();
                    var url = Path.getUri("api/quartz-jobs/exception-list/" + $scope.triggerName + "/" + $scope.triggerGroupName);
                    $http.get(url).success(function (largeLoad) {
                        data = largeLoad.filter(function (item) {
                            return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                        });
                        $scope.setPagingData(data, page, pageSize);
                    });
                } else {
                    var url = Path.getUri("api/quartz-jobs/exception-list/" + $scope.triggerName + "/" + $scope.triggerGroupName);

                    $http.get(url).success(function (largeLoad) {
                        $scope.setPagingData(largeLoad, page, pageSize);
                    });
                }
            }, 100);
        };

        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);

        $scope.$watch('pagingOptions', function (newVal, oldVal) {
            if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
            }
        }, true);
        $scope.$watch('filterOptions', function (newVal, oldVal) {
            if (newVal !== oldVal) {
                $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
            }
        }, true);

        $scope.gridOptions = {
            data: 'qrtzHistoryList',
            columnDefs: [
                {field: 'START_TIME', displayName: '开始时间'},
                {field: 'END_TIME', displayName: '结束时间'},
                {field: 'IS_NORMAL', displayName: '是否正常'},
                {field: 'EXCEPTION_DESC', displayName: '异常描述'}
                         ],
            enablePaging: true,
            showFooter: true,
            pagingOptions: $scope.pagingOptions,
            filterOptions: $scope.filterOptions
        };


    }
);

//jobApp.filter("quartzClassChange", function () {
//    return function (input) {
//        return input ? "quartz-content" : "";
//    }
//});

//jobApp.directive("mypopover", function () {
//    return function(scope, element, attr) {
//        element.popover({
//            content :
//                function() {
//                var html = element.children().html();
////                var template = angular.element(html);
////                $compile(template)(scope);
//                return html;
//            }
//        });
//    };
//}

//jobApp.directive("mypopover", function () {
//        return function (scope, element, attr) {
//            element.popover({
//                content: function () {
//                    var html = element.children.html();
////                    var template = angular.element(html);
////                    $compile(template)(scope);
//                    return html;
//                }
//            });
//        };
//    }

jobApp.directive("mypopover", ["$compile", function ($compile) {
    return function (scope, element, attr) {
        element.popover({
            content: function () {
                var template = angular.element('<div class="gridStyle" ng-grid="gridOptions"></div>');
//                var template = angular.element('<div ng-repeat="book in books">{{book}}</div>');
                $compile(template)(scope);
                console.info(template);
                return template;
            }

        });
    };
}]);