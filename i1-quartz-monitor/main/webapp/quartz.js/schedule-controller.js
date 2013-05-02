scheduleApp.controller('scheduleController', function scheduleController($scope, $http) {
        var self = this;
        $scope.serverlData = [
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1", triggerName: "ss", triggerGroupName: "11", cronExpress: "11"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1", triggerName: "ss", triggerGroupName: "11", cronExpress: "11"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1", triggerName: "ss", triggerGroupName: "11", cronExpress: "11"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1", triggerName: "ss", triggerGroupName: "11", cronExpress: "11"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1", triggerName: "ss", triggerGroupName: "11", cronExpress: "11"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1", triggerName: "ss", triggerGroupName: "11", cronExpress: "11"}

        ];
        $scope.gridServerData = { data: 'serverlData' };

        $scope.jobDetailData = [];
        $scope.filterOptions = {
            filterText: "",
            useExternalFilter: true
        };
        $scope.pagingOptions = {
            pageSizes: [5, 10, 15], //page Sizes
            pageSize: 5, //Size of Paging data
            totalServerItems: 0, //how many items are on the server (for paging)
            currentPage: 1 //what page they are currently on
        };
        self.getPagedDataAsync = function (pageSize, page, searchText) {

            var data;
            if (searchText) {
                var ft = searchText.toLowerCase();
                data = largeLoad().filter(function (item) {
                    return JSON.stringify(item).toLowerCase().indexOf(ft) != -1;
                });
            } else {
                data = largeLoad();
            }
            var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
            $scope.jobDetailData = pagedData;
            $scope.pagingOptions.totalServerItems = data.length;
            if (!$scope.$$phase) {
                $scope.$apply();
            }

        };
        $scope.$watch('pagingOptions', function () {
            self.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
        }, true);
        $scope.$watch('filterOptions', function () {
            self.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
        }, true);
        self.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);


        $scope.gridJobDetailData = {
            data: 'jobDetailData',
            enableCellEdit: true,
            enableCellSelection: true,
            enableColumnResize: true,
            enableColumnReordering: true,
            enablePaging: true,
            enableRowReordering: true,
            enableRowSelection: true,
            enableSorting: true,
            showFilter: true,
            showFooter: true,
            pagingOptions: $scope.pagingOptions,
            filterOptions: $scope.filterOptions
        };


        $scope.triggerData = [
            {triggerName: "Moroni", triggerGroupName: 50, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 51, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 52, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 53, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 54, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 55, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 56, cronExpress: "jobclass1"},
            {triggerName: "Moroni", triggerGroupName: 57, cronExpress: "jobclass1"}

        ];
        $scope.filterOptions1 = {
            filterText: "",
            useExternalFilter: true
        };
        $scope.pagingOptions1 = {
            pageSizes: [5, 10, 15], //page Sizes
            pageSize: 5, //Size of Paging data
            totalServerItems: 0, //how many items are on the server (for paging)
            currentPage: 1 //what page they are currently on
        };
        $scope.gridTriggerData = {
            data: 'triggerData',
            enableCellEdit: true,
            enableCellSelection: true,
            enableColumnResize: true,
            enableColumnReordering: true,
            enablePaging: true,
            enableRowReordering: true,
            enableRowSelection: true,
            enableSorting: true,
            showFilter: true,
            showFooter: true,
            pagingOptions: $scope.pagingOptions1,
            filterOptions: $scope.filterOptions1
        };

        $scope.listenerData = [
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"},
            {jobname: "Moroni", jobgroupname: 50, jobclass: "jobclass1"}
        ];
        $scope.gridListenerData = { data: 'listenerData' };


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
//    $scope.schedule = {
//        $scope.jobName = "";
//    $scope.jobClass = ""
//    $scope.triggerName = ""
//    $scope.cronexpress = ""
//    $scope.url = ""
//} ;
        $scope.currentSchedule = {
            jobName: "",
            jobClass: "",
            triggerName: "",
            cronExpress: "",
            url: ""
        };
        var setCurrentSchedule = function () {
            $scope.currentSchedule.jobName = $scope.jobName;
            $scope.currentSchedule.jobClass = $scope.jobClass;
            $scope.currentSchedule.triggerName = $scope.triggerName;
            $scope.currentSchedule.cronExpress = $scope.cronExpress;
            $scope.currentSchedule.url = $scope.url;
        }

        $scope.saveSchedule = function () {
            setCurrentSchedule();
            var url = ""
            $http.post(url, $scope.currentSchedule).success(
                function (data, status, headers, config) {

                }).error(function (data, status, headers, config) {
                    alert(data.message);
                });
        }
    }
)
;
