var JobVO = function () {
    this.jobDetail = [];
    this.triggers = [];
};

var JobDetail = function () {
    this.jobName = "";
    this.jobGroupName = "";
//    this.jobClass = "com.thoughtworks.i1.quartz.jobs.JobForTest";
    this.jobClass = "com.thoughtworks.i1.quartz.jobs.JobForUrl";
    this.description = "";
    this.jobDatas = [];
}   ;

var Trigger = function () {
    this.triggerName = "";
    this.triggerGroupName = "";
    this.startTime = 0;
    this.endTime = 0;
    this.triggerState = "";
    this.repeatCount = -1;
    this.repeatInterval = 10000;
};

var JobData = function (key, value) {
    this.key = key;
    this.value = value;
};

JobVO.prototype.addJobDetail = function (jobDetail) {
    this.jobDetail.push(jobDetail)
};

JobVO.prototype.addTrigger = function (trigger) {
    this.triggers.push(trigger)
};

JobDetail.prototype.addJobData = function (jobData) {
    this.jobDatas.push(jobData);
};


JobVO.prototype.copyJobVO = function () {
    for (i in data) {
        this[i] = data[i];
    }
    return this;
};







