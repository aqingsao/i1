package com.thoughtworks.i1.quartz.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.quartz.domain.JobDataVO;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

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
            JobDetail jobDetail = getJobDetail(quartzVO);
            this.addJob(jobDetail);

            List<TriggerVO> triggerVOs = quartzVO.getTriggers();
            for(TriggerVO triggerVO : triggerVOs){
                Trigger trigger = getTrigger(jobDetail, triggerVO);

                scheduleJob(trigger);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private Trigger getTrigger(JobDetail jobDetail, TriggerVO triggerVO) {
        String triggerGroupName = triggerVO.getTriggerGroupName();
        TriggerBuilder<Trigger> triggerBuilder = newTrigger()
                .withIdentity(triggerVO.getTriggerName(), triggerGroupName.length() == 0 ? "HEREN-TRIGGER-GROUP" : triggerGroupName)
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
        return triggerBuilder.build();
    }

    private JobDetail getJobDetail(QuartzVO quartzVO) throws ClassNotFoundException {
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(quartzVO.getJobClass());
        String jobGroupName = quartzVO.getJobGroupName();
        JobBuilder jobBuilder = newJob(jobClass)
                .withIdentity(quartzVO.getJobName(), jobGroupName.length() == 0 ? "HEREN-JOB-GROUP" : jobGroupName)
                .withDescription(quartzVO.getDescription())
                .storeDurably(true);
        List<JobDataVO> jobDatas = quartzVO.getJobDatas();
        for (JobDataVO jobDataVO : jobDatas) {
            String key = jobDataVO.getKey();
            String value = jobDataVO.getValue();
            jobBuilder.usingJobData(key, value);
        }
        return jobBuilder.build();
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
        List<JobDataVO> jobDatas = Lists.newArrayList();
        for (String key : jobDataMap.getKeys()) {
            JobDataVO jobDataVO = new JobDataVO(key, jobDataMap.get(key).toString());
            jobDatas.add(jobDataVO);
        }
        quartzVO.setJobDatas(jobDatas);
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