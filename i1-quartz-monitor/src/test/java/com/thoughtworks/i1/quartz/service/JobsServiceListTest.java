package com.thoughtworks.i1.quartz.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.quartz.domain.JobVO;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobsServiceListTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new QuartzModule());
        JobsService jobsService = injector.getInstance(JobsService.class);
        try{
            List<JobVO> jobVOList = jobsService.getJobVOs();
            for(JobVO jobVO : jobVOList){
                System.out.println("JobDetail.key=" + jobVO.getJobDetail().getKey());
                System.out.println("JobDetail.jobClass=" + jobVO.getJobDetail().getJobClass());
                JobDataMap jobDataMap = jobVO.getJobDetail().getJobDataMap();
                for (String key : jobDataMap.getKeys()) {
                    System.out.println("jobDataMap.key=" + key);
                    System.out.println("jobDataMap.value=" + jobDataMap.getString(key));
                }
                List<? extends Trigger> triggers = jobVO.getTriggers();
                for(Trigger trigger : triggers){
                    System.out.println("trigger.key=" + trigger.getKey());
                    System.out.println("trigger.startTime=" + trigger.getStartTime());
                }

            }
            sleep(5000);
            jobsService.shutdown();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
