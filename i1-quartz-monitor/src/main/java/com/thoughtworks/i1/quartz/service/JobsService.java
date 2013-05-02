package com.thoughtworks.i1.quartz.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.i1.quartz.commons.SystemException;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

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

    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(trigger);
    }

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
        List<JobVO> jobVOs = getJobVOs();
        List<QuartzVO> quartzVOs = Lists.newArrayList();

        for (JobVO jobVO : jobVOs) {
            QuartzVO quartzVO = getQuartzVOFromJob(jobVO.getJobDetail());

            List<TriggerVO> triggerVOs = getTriggerVOFromJob(jobVO.getTriggers());
            quartzVO.setTriggers(triggerVOs);

            quartzVOs.add(quartzVO);
        }

        return quartzVOs;
    }

    public void saveJob(QuartzVO quartzVO) {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(quartzVO.getJobClass());
            JobBuilder jobBuilder = newJob(jobClass)
                    .withIdentity(quartzVO.getJobName(), quartzVO.getJobGroupName())
                    .withDescription(quartzVO.getDescription())
                    .storeDurably(true);
            Map<String, String> jobData = quartzVO.getJobData();
            for (String key : jobData.keySet()) {
                String value = jobData.get(key);
                jobBuilder.usingJobData(key, value);
            }
            JobDetail jobDetail = jobBuilder.build();
            this.addJob(jobDetail);

            List<TriggerVO> triggerVOs = quartzVO.getTriggers();
            for(TriggerVO triggerVO : triggerVOs){
                TriggerBuilder<Trigger> triggerBuilder = newTrigger()
                        .withIdentity(triggerVO.getTriggerName(), triggerVO.getTriggerGroupName())
                        .startAt(triggerVO.getStartTime())
                        .endAt(triggerVO.getEndTime())
                        .forJob(jobDetail)
                       ;
                triggerBuilder.withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withRepeatCount(triggerVO.getRepeatCount())
                                .withIntervalInMilliseconds(triggerVO.getRepeatInterval())
                );
                Trigger trigger = triggerBuilder.build();

                scheduleJob(trigger);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private List<TriggerVO> getTriggerVOFromJob(List<? extends Trigger> triggers) {
        List<TriggerVO> triggerVOs = Lists.newArrayList();
        for (Trigger trigger : triggers) {
            TriggerVO triggerVO = new TriggerVO();
            triggerVO.setTriggerName(trigger.getKey().getName());
            triggerVO.setTriggerGroupName(trigger.getKey().getGroup());
            triggerVO.setStartTime(trigger.getStartTime());
            triggerVO.setEndTime(trigger.getEndTime());
            triggerVOs.add(triggerVO);
        }
        return triggerVOs;
    }

    private QuartzVO getQuartzVOFromJob(JobDetail jobDetail) {
        QuartzVO quartzVO = new QuartzVO();
        quartzVO.setJobName(jobDetail.getKey().getName());
        quartzVO.setJobGroupName(jobDetail.getKey().getGroup());
        quartzVO.setJobClass(jobDetail.getJobClass().getName());
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Map<String, String> jobData = Maps.newHashMap();
        for (String key : jobDataMap.getKeys()) {
            jobData.put(key, jobDataMap.get(key).toString());
        }
        quartzVO.setJobData(jobData);
        return quartzVO;
    }

    public List<JobVO> getJobVOs() {
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