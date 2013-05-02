package com.thoughtworks.i1.quartz.domain;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;

public class JobVO {
    private JobDetail jobDetail;
    private List<? extends Trigger> triggers;

    public JobVO(JobDetail jobDetail, List<? extends Trigger> triggers) {
        this.jobDetail = jobDetail;
        this.triggers = triggers;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public List<? extends Trigger> getTriggers() {
        return triggers;
    }
}
