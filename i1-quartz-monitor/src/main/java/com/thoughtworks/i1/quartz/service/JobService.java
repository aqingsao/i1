package com.thoughtworks.i1.quartz.service;

import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.quartz.domain.JobDetailVO;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        LOGGER.info(String.format("Scheduling job(%s, %s) with trigger(%s, %s)"
                , jobDetail.getKey().getGroup(), jobDetail.getKey().getName(),
                trigger.getKey().getGroup(), trigger.getKey().getName()));
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(trigger);
    }

    @Transactional
    public void addJob(JobDetail jobDetail) throws SchedulerException {
        scheduler.addJob(jobDetail, false);
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

    public List<QuartzVO> findAllJobs() {
        try {
            List<QuartzVO> quartzVOs = Lists.newArrayList();
            for (JobDetail jobDetail : getJobDetails()) {
                quartzVOs.add(getQuartzVO(jobDetail));
            }

            return quartzVOs;
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    private QuartzVO getQuartzVO(JobDetail jobDetail) throws SchedulerException {
        List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobDetail.getKey());
        List<TriggerVO> triggerVOs = Lists.newArrayList();
        for (SimpleTrigger trigger : (List<SimpleTrigger>) triggersOfJob) {
            triggerVOs.add(new TriggerVO(trigger, scheduler.getTriggerState(trigger.getKey())));
        }
        return new QuartzVO(new JobDetailVO(jobDetail), triggerVOs);
    }

    @Transactional
    public void saveJob(QuartzVO quartzVO) {
        try {
            JobDetail jobDetail = quartzVO.getJobDetail();
            scheduler.addJob(jobDetail, false);

            List<TriggerVO> triggerVOs = quartzVO.getTriggers();
            for (TriggerVO triggerVO : triggerVOs) {
                scheduleJob(triggerVO.toTrigger(jobDetail.getKey()));
            }

        } catch (Exception e) {
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

    /**
     * 检查调度是否启动
     *
     * @return
     * @throws SchedulerException
     */
    public boolean isStarted() throws SchedulerException {
        return scheduler.isStarted();
    }


    public void pasuseTrigger(String triggerName, String triggerGroupName) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        scheduler.pauseTrigger(triggerKey);//停止触发器
    }

    public void deleteTrigger(String triggerName, String triggerGroupName) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        scheduler.unscheduleJob(triggerKey);//移除触发器

    }

    public void resumeTrigger(String triggerName, String triggerGroupName) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        scheduler.resumeTrigger(triggerKey);
    }

    public void deleteJob(String jobName, String jobGroupName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        scheduler.deleteJob(jobKey);
    }


}