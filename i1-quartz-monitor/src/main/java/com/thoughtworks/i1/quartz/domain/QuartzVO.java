package com.thoughtworks.i1.quartz.domain;

import java.util.List;
import java.util.Map;

public class QuartzVO {

    private String jobName;
    private String jobGroupName;
    private String description;
    private String jobClass;
    private List<JobDataVO> jobDatas;
    private List<TriggerVO> triggers;

    public QuartzVO() {
    }

    public QuartzVO(String jobName, String jobGroupName, String description, String jobClass, List<JobDataVO> jobDatas, List<TriggerVO> triggers) {
        this.jobName = jobName;
        this.jobGroupName = jobGroupName;
        this.description = description;
        this.jobClass = jobClass;
        this.jobDatas = jobDatas;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public List<JobDataVO> getJobDatas() {
        return jobDatas;
    }

    public void setJobDatas(List<JobDataVO> jobDatas) {
        this.jobDatas = jobDatas;
    }

    public List<TriggerVO> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerVO> triggers) {
        this.triggers = triggers;
    }
}
