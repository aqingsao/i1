var Quartz = function () {
    this.jobName = "";
    this.jobGroupName = "";
    this.jobClass = "com.thoughtworks.i1.quartz.jobs.JobForTest";
    this.description = "";
    this.jobDatas = [];
    this.triggers = [];
};

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




Quartz.prototype.addTrigger = function (trigger) {
    this.triggers.push(trigger);
};

Quartz.prototype.addJobData = function(key, value){
    var tempJobData = new JobData(key, value);
    this.jobDatas.push(tempJobData);
};

Quartz.prototype.copyQuartzVO = function(data){
    for(i in data){
        this[i] = data[i];
    }
    return this;
};




