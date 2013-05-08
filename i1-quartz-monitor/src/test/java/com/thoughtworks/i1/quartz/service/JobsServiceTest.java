package com.thoughtworks.i1.quartz.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.quartz.QuartzApplication;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobsServiceTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new QuartzApplication.QuartzModule());
        JobsService jobsService = injector.getInstance(JobsService.class);
        try{
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName("com.thoughtworks.i1.quartz.jobs.JobForTest");
            JobDetail jobDetail = newJob(jobClass)
                    .withIdentity("jobName", "jobGroupName")
                    .usingJobData("url", "test-url")
                    .storeDurably(true)
                    .build();
            jobsService.addJob(jobDetail);

            Trigger trigger = newTrigger()
                    .withIdentity("triggerName", "triggerGroupName")
                    .withSchedule(cronSchedule("0/5 * * * * ? "))
                    .forJob(jobDetail)
                    .build();
            jobsService.scheduleJob(trigger);

            Trigger trigger2 = newTrigger()
                    .withIdentity("triggerName2", "triggerGroupName")
                    .withSchedule(cronSchedule("0/3 * * * * ? "))
                    .forJob(jobDetail)
                    .build();
            jobsService.scheduleJob(trigger2);

            sleep(20000);
            jobsService.shutdown();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
