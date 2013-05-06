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

        var TriggerInfo = function () {
            startTime = "";
            endTime = "";
            repeatInterval = 0;
            repeatIntervalUnit = 0;
        }

        $scope.units = [
            {"key":0,"value":"秒"},
            {"key":1,"value":"分钟"},
            {"key":2,"value":"小时"},
            {"key":3,"value":"天"}
        ];

        $scope.triggerInfoList = [];
        $scope.triggerInfoList.push(new TriggerInfo());

//        初始化
        $scope.quartzList = [];

        $scope.quartz = new Quartz();

        $scope.quartz.addTrigger(new Trigger());

        $scope.saveQuartz = function () {

            $scope.quartz.addJobData("url", $scope.url);

            for (i = 0; i < $scope.triggerInfoList.length; i++) {
                var triggerInfoTemp = $scope.triggerInfoList[i];
                var startDatetime = new Date(triggerInfoTemp.startTime).getTime();
                $scope.quartz.triggers[i].startTime = startDatetime;
                var endDatetime = new Date(triggerInfoTemp.endTime).getTime();
                $scope.quartz.triggers[i].endTime = endDatetime;

                $scope.quartz.triggers[i].repeatInterval = getRepeatInterval(triggerInfoTemp.repeatInterval, triggerInfoTemp.repeatIntervalUnit);

            }


            tempurl = Path.getUri("api/quartz-jobs/item");
            $http.post(tempurl, $scope.quartz).success(
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
            $scope.triggerInfoList.push(new TriggerInfo());
        }

        $scope.listQuartz();

        $scope.getRepeatIntervalExpress = function(repeatInterval){
            var temp = 0;
            var tempUnit = "秒"
            if(repeatInterval > 0){
                if(repeatInterval % (12 * 60 * 60 * 1000) === 0){
                    temp = repeatInterval / (12 * 60 * 60 * 1000);
                    tempUnit = "天"
                } else if(repeatInterval % (60 * 60 * 1000) === 0){
                    temp = repeatInterval / (60 * 60 * 1000);
                    tempUnit = "小时"
                } else if(repeatInterval % (60 * 1000) === 0){
                    temp = repeatInterval / (60 * 1000);
                    tempUnit = "分钟"
                } else{
                    temp = repeatInterval / (1000);
                    tempUnit = "秒"
                }
            }

            return temp + tempUnit;
        }

        function getRepeatInterval(repeatInterval, repeatIntervalUnit){
            var temp = 0;
            if(repeatIntervalUnit === 0){
                return repeatInterval * 1000;
            } else if(repeatIntervalUnit === 1){
                return repeatInterval * 60 * 1000;
            } else if(repeatIntervalUnit === 2){
                return repeatInterval * 60 * 60 * 1000;
            } else if(repeatIntervalUnit === 3){
                return repeatInterval * 12 * 60 * 60 * 1000;
            }
            return temp;
        }
    }
)
;
