package com.thoughtworks.i1.quartz.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.commons.test.TransactionalDomainTestRunner;
import com.thoughtworks.i1.quartz.QuartzApplication;
import com.thoughtworks.i1.quartz.api.QuartzTestApplication;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.thoughtworks.i1.quartz.domain.QuartzVO.QuartzVOBuilder.aQuartzVO;
import static java.lang.Thread.sleep;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@RunWith(TransactionalDomainTestRunner.class)
@RunWithApplication(QuartzTestApplication.class)
public class JobServiceTest {
    @Inject
    private JobService jobService;

    @Test
    public void should_save_a_job() throws Exception {
        QuartzVO quartzVO = aQuartzVO().jobDetail("name", "group", "com.thoughtworks.i1.quartz.DummyJob").end()
                .addTrigger("a", "herenTrigger").time(new Date(), new Date()).end().build();
        jobService.saveJob(quartzVO);

        List<QuartzVO> actual = jobService.findAllJobs();
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0).getJobName(), is("name"));
        assertThat(actual.get(0).getJobGroupName(), is("group"));
        assertThat(actual.get(0).getJobClass(), is("com.thoughtworks.i1.quartz.DummyJob"));
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new QuartzApplication.QuartzModule());
        JobService jobService = injector.getInstance(JobService.class);
        try{
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName("com.thoughtworks.i1.quartz.jobs.JobForTest");
            JobDetail jobDetail = newJob(jobClass)
                    .withIdentity("jobName", "jobGroupName")
                    .usingJobData("url", "test-url")
                    .storeDurably(true)
                    .build();
            jobService.addJob(jobDetail);

            Trigger trigger = newTrigger()
                    .withIdentity("triggerName", "triggerGroupName")
                    .withSchedule(cronSchedule("0/5 * * * * ? "))
                    .forJob(jobDetail)
                    .build();
            jobService.scheduleJob(trigger);

            Trigger trigger2 = newTrigger()
                    .withIdentity("triggerName2", "triggerGroupName")
                    .withSchedule(cronSchedule("0/3 * * * * ? "))
                    .forJob(jobDetail)
                    .build();
            jobService.scheduleJob(trigger2);

            sleep(20000);
            jobService.shutdown();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
