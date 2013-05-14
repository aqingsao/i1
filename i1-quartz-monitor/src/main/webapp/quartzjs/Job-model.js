var JobVO = function () {
    this.detail = {};
    this.triggers = [];
};

var JobDetailVO = function () {
    this.jobName = "";
    this.jobGroupName = "";
//    this.jobClass = "com.thoughtworks.i1.quartz.jobs.JobForTest";
    this.jobClass = "com.thoughtworks.i1.quartz.jobs.JobForUrl";
    this.description = "";
    this.jobDatas = [];
}   ;

var TriggerVO = function () {
    this.triggerName = "";
    this.triggerGroupName = "";
    this.startTime = 0;
    this.endTime = 0;
    this.triggerState = "";
    this.repeatCount = -1;
    this.repeatInterval = 10000;
};

var JobDataVO = function (key, value) {
    this.key = key;
    this.value = value;
};

JobVO.prototype.addJobDetail = function (detail) {
    this.detail = detail;
};

JobVO.prototype.addTrigger = function (trigger) {
    this.triggers.push(trigger)
};

JobDetailVO.prototype.addJobData = function (key, value) {
    var tempJobData = new JobDataVO(key, value);
    this.jobDatas.push(tempJobData);
};


JobVO.prototype.copyJob = function (data) {
    for (i in data) {
        this[i] = data[i];
    }
    return this;
};







