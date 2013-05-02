package com.thoughtworks.i1.quartz.service;

import com.google.common.collect.Lists;
import com.thoughtworks.i1.quartz.commons.SystemException;
import com.thoughtworks.i1.quartz.domain.JobVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class JobsService {
    private final Scheduler scheduler;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsService.class);

    @Inject
    public JobsService(final SchedulerFactory factory, final GuiceJobFactory jobFactory) throws SchedulerException {
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        LOGGER.info("Starting scheduler...");
        scheduler.start();
        LOGGER.info("Scheduler started successfully...");
    }

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        LOGGER.info(String.format("Scheduling job(%s, %s) with trigger(%s, %s)"
                , jobDetail.getKey().getGroup(), jobDetail.getKey().getName(),
                trigger.getKey().getGroup(), trigger.getKey().getName()));
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    public void shutdown() {
        try {
            LOGGER.info("Shutting down scheduler...");
            scheduler.shutdown();
            LOGGER.info("Shut down scheduler successfully...");
        } catch (SchedulerException e) {
            LOGGER.warn("Failed to shutdown Quartz scheduler: " + e.getMessage(), e);
        }
    }

    public List<JobVO> findAllJobs() {

        try {
            List<JobVO> jobVOs = Lists.newArrayList();
            for (JobDetail jobDetail : getJobDetails()) {
                jobVOs.add(new JobVO(jobDetail, scheduler.getTriggersOfJob(jobDetail.getKey())));
            }
            return jobVOs;
        } catch (SchedulerException e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    private List<JobDetail> getJobDetails() throws SchedulerException {
        List<JobDetail> jobDetails = new ArrayList<>();

        List<String> jobGroupNames = scheduler.getJobGroupNames();
        for (String jobGroupName : jobGroupNames) {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName));
            for (JobKey jobKey : jobKeys) {
                jobDetails.add(scheduler.getJobDetail(jobKey));
            }
        }
        return jobDetails;
    }
}