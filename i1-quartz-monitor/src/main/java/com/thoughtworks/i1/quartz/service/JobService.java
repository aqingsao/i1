package com.thoughtworks.i1.quartz.service;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.thoughtworks.i1.quartz.domain.JobDetailVO.fromJobDetail;
import static com.thoughtworks.i1.quartz.domain.TriggerVO.fromTrigger;

public class JobService {
    private final Scheduler scheduler;
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    @Inject
    public JobService(final SchedulerFactory factory, final GuiceJobFactory jobFactory) throws SchedulerException {
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        LOGGER.info("Starting scheduler...");
        scheduler.start();
        LOGGER.info("Scheduler started successfully...");
    }

    public List<JobVO> findAllJobs() {
        try {
            List<JobVO> jobVOs = Lists.newArrayList();
            for (JobDetail jobDetail : getJobDetails()) {
                jobVOs.add(getJobVO(jobDetail));
            }

            return jobVOs;
        } catch (Exception e) {
            throw new SystemException(String.format("Failed to find all jobs: %s", e.getMessage()), e);
        }
    }

    public Optional<JobVO> findJob(String jobName, String jobGroupName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
            if (jobDetail == null) {
                return Optional.absent();
            }

            return Optional.of(getJobVO(jobDetail));
        } catch (SchedulerException e) {
            throw new SystemException(String.format("Failed to find job: %s", e.getMessage()), e);
        }
    }

    @Transactional
    public void saveJob(JobVO jobVO) {
        try {
            JobDetail jobDetail = jobVO.getJobDetail();
            scheduler.addJob(jobDetail, false);

            List<TriggerVO> triggerVOs = jobVO.getTriggers();
            for (TriggerVO triggerVO : triggerVOs) {
                scheduler.scheduleJob(triggerVO.toTrigger(jobDetail.getKey()));
            }
        } catch (SchedulerException e) {
            throw new SystemException(String.format("Failed to save job: %s", e.getMessage()), e);
        }
    }

    @Transactional
    public void deleteJob(String jobName, String jobGroupName) {
        try {
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (SchedulerException e) {
            throw new SystemException(String.format("Failed to delete job: %s", e.getMessage()), e);
        }
    }

    public void pasuseTrigger(String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            scheduler.pauseTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new SystemException(String.format("Failed to pause trigger", e.getMessage()), e);
        }
    }

    public void resumeTrigger(String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            scheduler.resumeTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new SystemException(String.format("Failed to resume trigger: %s", e.getMessage()), e);
        }
    }

    public void deleteTrigger(String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try {
            scheduler.unscheduleJob(triggerKey);
        } catch (SchedulerException e) {
            throw new SystemException(String.format("Failed to delete trigger: %s", e.getMessage()), e);
        }
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

    public Scheduler getScheduler() {
        return scheduler;
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

    private JobVO getJobVO(JobDetail jobDetail) throws SchedulerException {
        JobVO jobVO = new JobVO(fromJobDetail(jobDetail));
        for (SimpleTrigger trigger : (List<SimpleTrigger>) scheduler.getTriggersOfJob(jobDetail.getKey())) {
            jobVO.addTriggerVO(fromTrigger(trigger, scheduler.getTriggerState(trigger.getKey()).name()));
        }
        return jobVO;
    }
}