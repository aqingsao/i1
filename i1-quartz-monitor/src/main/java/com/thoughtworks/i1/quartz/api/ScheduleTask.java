package com.thoughtworks.i1.quartz.api;

import com.thoughtworks.i1.quartz.service.JobsService;

import javax.inject.Inject;

public class ScheduleTask {
    private JobsService jobsService;
    @Inject
    public ScheduleTask(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    public static void main(String[] args) {

    }
}
