package com.thoughtworks.i1.quartz.domain;

import java.util.List;
import java.util.Map;

public class QuartzVO {

    private String jobName;
    private String jobGroupName;
    private String jobClass;
    private Map<String, String> jobData;
    private List<TriggerVO> triggers;

    public QuartzVO() {
    }

    public QuartzVO(String jobName, String jobGroupName, String jobClass, Map<String, String> jobData, List<TriggerVO> triggers) {
        this.jobName = jobName;
        this.jobGroupName = jobGroupName;
        this.jobClass = jobClass;
        this.jobData = jobData;
        this.triggers = triggers;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Map<String, String> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, String> jobData) {
        this.jobData = jobData;
    }

    public List<TriggerVO> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerVO> triggers) {
        this.triggers = triggers;
    }
}
