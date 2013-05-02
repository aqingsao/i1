package com.thoughtworks.i1.quartz;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.quartz.service.JobsService;
import com.thoughtworks.i1.quartz.service.QuartzModule;
import org.quartz.*;

import javax.inject.Inject;

public class Main {
    public static void main(final String[] args) {
        final Injector i = Guice.createInjector(new QuartzModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(FooJobActivator.class).asEagerSingleton();
            }
        });
        System.out.println("Guice ready, waiting 130 secs now...");

        try {
            Thread.sleep(130 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("shutdown");
        i.getInstance(JobsService.class).shutdown();
        System.out.println("done");
    }

    static class FooJob implements Job {
        public FooJob() {
            System.out.println(this + " was created");
        }

        @Override
        public void execute(final JobExecutionContext arg0) throws JobExecutionException {
            System.out.println(this + " was run!");
        }
    }

    static class FooJobActivator {
        @Inject
        public FooJobActivator(final JobsService q) throws SchedulerException {
            JobDetail jobDetail = JobBuilder.newJob(FooJob.class).withIdentity("myFootJob", "myGroup").build();
//            jobDetail.getJobDataMap().put
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTriggerName", "myTriggerGroup")
                    .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever())
                    .startAt(DateBuilder.futureDate(0, DateBuilder.IntervalUnit.MINUTE))
                    .build();

            q.scheduleJob(jobDetail, trigger);
        }
    }
}