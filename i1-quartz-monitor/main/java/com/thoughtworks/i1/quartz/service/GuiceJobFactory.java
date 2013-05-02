package com.thoughtworks.i1.quartz.service;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;

final class GuiceJobFactory implements JobFactory
{
    private final Injector injector;

    @Inject
    public GuiceJobFactory(final Injector injector)
    {
        this.injector = injector;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = bundle.getJobDetail();
        Class jobClass = jobDetail.getJobClass();
        return (Job) injector.getInstance(jobClass);
    }
}