package com.thoughtworks.i1.quartz.service;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobsServiceTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new QuartzModule());
        JobsService jobsService = injector.getInstance(JobsService.class);
        try{
            JobDetail jobDetail = newJob(JobForTest.class)
                    .withIdentity("jobName", "jobGroupName")
                    .usingJobData("url", "test-url")
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity("triggerName", "triggerGroupName")
                    .withSchedule(cronSchedule("0/5 * * * * ? "))
                    .build();
            jobsService.scheduleJob(jobDetail, trigger);

            sleep(20000);
            jobsService.shutdown();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
