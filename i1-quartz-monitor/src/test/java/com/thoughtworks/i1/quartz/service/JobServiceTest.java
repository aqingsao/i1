package com.thoughtworks.i1.quartz.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.commons.test.TransactionalDomainTestRunner;
import com.thoughtworks.i1.quartz.QuartzApplication;
import com.thoughtworks.i1.quartz.api.QuartzTestApplication;
import com.thoughtworks.i1.quartz.domain.JobVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.thoughtworks.i1.quartz.domain.JobVO.QuartzVOBuilder.aQuartzVO;
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
        JobVO jobVO = aQuartzVO().jobDetail("name", "group", "com.thoughtworks.i1.quartz.DummyJob").end()
                .addTrigger("a", "herenTrigger").time(new Date(), new Date()).end().build();
        jobService.saveJob(jobVO);

        List<JobVO> actual = jobService.findAllJobs();
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0).getJobName(), is("name"));
        assertThat(actual.get(0).getJobGroupName(), is("group"));
        assertThat(actual.get(0).getJobClass(), is("com.thoughtworks.i1.quartz.DummyJob"));
    }
}
