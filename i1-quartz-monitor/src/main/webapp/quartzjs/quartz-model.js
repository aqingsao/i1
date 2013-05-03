var Quartz = function () {
    this.jobName = "";
    this.jobGroupName = "";
    this.jobClass = "";
    this.description = "";
    this.url = "";
    map()
    this.triggerList = [];
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

Quartz.prototype.addTrigger = function (trigger) {
    this.triggerList.push(trigger);
};



function Map() {
    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();
    /**         * 放入一个键值对        * @param {String} key        * @param {Object} value        */
    this.put = function (key, value) {
        if (this.data[key] == null) {
            this.keys.push(key);
        }
        this.data[key] = value;
    };
}


