var Job = function () {
    this.trigger = [];
    this.jobdetail = []
};

var JobDetail = function () {
    this.jobName = "";
    this.jobgroupname = "";
    this.jobClass = "";
    this.jobData = []
};

var Trigger = function () {
    this.triggerName = "";
    this.triggerGroupName = "";
    this.startTime = null;
    this.endTime = null;
    this.triggerState = "";
    this.repeatCount = 12;
    this.repeatInterval = 12;
};

var jobData = function () {
    this.key = "";
    this.value = "";
};

Job.prototype.addJobDetail = function(jobDetail){
    this.jobDetail.push(jobDetail);
}   ;

Job.prototype.addTrigger = function(trigger){
    this.trigger.push(trigger)
}   ;

