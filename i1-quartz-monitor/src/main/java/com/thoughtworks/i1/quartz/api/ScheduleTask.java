package com.thoughtworks.i1.quartz.api;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class ScheduleTask {

    public static void main(String[] args) throws Exception {
        String jobName = "jobName";
        String jobGroupName = "jobGroupName";
        String jobClass = "com.thoughtworks.i1.quartz.jobs.JobForUrl";

        String cronExpress = "0/3 * * * * ? ";
        String triggerName = "triggerName";
        String triggerGroupName = "triggerGroupName";

        String url1 = "http://localhost:8051/heren/api/dept-rel-index/dept-code-presc-type/100/1";

        runSchedule(jobName, jobGroupName, jobClass, cronExpress, triggerName, triggerGroupName, url1);

    }

    static void runSchedule(String jobName, String jobGroupName, String jobClass, String cronExpress,
                            String triggerName, String triggerGroupName, String url) throws Exception {

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        Class jclass = Class.forName(jobClass);
        scheduler.start();

        if (scheduler.isStarted()) {
            JobDetail jobDetail1 = newJob(jclass)
                    .withIdentity(jobName, jobGroupName)
                    .usingJobData("url", url)
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(cronSchedule(cronExpress))
                    .build();
            scheduler.scheduleJob(jobDetail1, trigger);
        }

    }

}
