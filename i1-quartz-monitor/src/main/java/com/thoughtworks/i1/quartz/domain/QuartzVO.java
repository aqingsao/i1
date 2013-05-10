package com.thoughtworks.i1.quartz.domain;

import com.google.common.collect.Lists;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import java.util.List;

public class QuartzVO {

    private String jobName;
    private String jobGroupName;
    private String description;
    private String jobClass;
    private List<JobDataVO> jobDatas;
    private List<TriggerVO> triggers;

    public QuartzVO() {
    }

    public QuartzVO(JobDetail jobDetail) {
        this.jobName = jobDetail.getKey().getName();
        this.jobGroupName = jobDetail.getKey().getGroup();
        this.jobClass = jobDetail.getJobClass().getName();
        this.jobDatas = Lists.newArrayList();
        for (String key : jobDetail.getJobDataMap().getKeys()) {
            jobDatas.add(new JobDataVO(key, jobDetail.getJobDataMap().get(key).toString()));
        }
    }

    public static QuartzVO getQuartzVO1(JobDetail jobDetail) {

        return new QuartzVO(jobDetail);
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
