package com.thoughtworks.i1.quartz.service;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.commons.test.TransactionalDomainTestRunner;
import com.thoughtworks.i1.quartz.api.QuartzTestApplication;
import com.thoughtworks.i1.quartz.domain.JobVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.thoughtworks.i1.commons.util.DateHelper.*;
import static com.thoughtworks.i1.quartz.domain.JobVO.QuartzVOBuilder.aJobVO;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(TransactionalDomainTestRunner.class)
@RunWithApplication(QuartzTestApplication.class)
public class JobServiceTest {
    public static final String TEST_JOB_GROUP = "test_group";
    @Inject
    private JobService jobService;
    private JobVO savedJobWith1Trigger;
    private JobVO jobWith2Triggers;
    private static JobResults jobResults = new JobResults();

    @Before

    public void before() throws SchedulerException {
        cleanAllJobs();
        savedJobWith1Trigger = aJobVO().jobDetail("name1", TEST_JOB_GROUP, DummyJob.class.getName() ,"desc").end()
                .addTrigger("trigger11", "triggerGroup1").time(today(), tomorrow()).repeat(minutes(1), 1).end()
                .build();
        jobWith2Triggers = aJobVO().jobDetail("name2", TEST_JOB_GROUP, DummyJob.class.getName(), "desc").end()
                .addTrigger("trigger21", "triggerGroup2").time(today(), tomorrow()).repeat(minutes(1), 1).end()
                .addTrigger("trigger22", "triggerGroup2").time(today(), tomorrow()).repeat(minutes(2), 1).end()
                .build();
        jobService.createJob(savedJobWith1Trigger);
    }

    @Test
    public void should_save_job_successfully() {
        jobService.createJob(jobWith2Triggers);

        Optional<JobVO> jobVO = jobService.findJob(jobWith2Triggers.getJobDetailVO().getJobName(), jobWith2Triggers.getJobDetailVO().getJobGroupName());
        assertThat(jobVO.isPresent(), is(true));
        assertThat(jobVO.get().getJobClass(), is(jobWith2Triggers.getJobDetailVO().getJobClass()));
    }

    @Test
    public void should_throw_exception_when_job_name_and_group_name_already_exists() {
        try {
            jobService.createJob(savedJobWith1Trigger);
            fail("Should fail as job is already saved in before() method");
        } catch (SystemException e) {
            assertThat(e.getMessage().startsWith("Failed to save job"), is(true));
        }
    }

    @Test
    public void should_throw_exception_when_job_is_already_saved() throws Exception {
        JobVO jobWithDuplicateName = aJobVO().jobDetail(savedJobWith1Trigger.getJobDetailVO().getJobName(), savedJobWith1Trigger.getJobDetailVO().getJobGroupName(), DummyJob.class.getName(), "desc").end()
                .addTrigger("trigger31", "triggerGroup3").time(today(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        try {
            jobService.createJob(jobWithDuplicateName);
            fail("Should fail as job name and group name is duplicate with existing ones");
        } catch (SystemException e) {
            assertThat(e.getMessage().startsWith("Failed to save job"), is(true));
        }
    }

    @Test
    public void should_throw_exception_when_job_class_name_cannot_be_found() throws Exception {
        JobVO jobWithDuplicateName = aJobVO().jobDetail("test", TEST_JOB_GROUP, "job.class.undefined", "desc").end()
                .addTrigger("trigger31", "triggerGroup3").time(today(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        try {
            jobService.createJob(jobWithDuplicateName);
            fail("Should fail as job name cannot be found");
        } catch (SystemException e) {
            assertThat(e.getMessage().startsWith("Cannot find job with name"), is(true));
        }
    }

    @Test
    public void should_delete_a_job_successfully() {
        jobService.deleteJob(savedJobWith1Trigger.getJobDetailVO().getJobName(), savedJobWith1Trigger.getJobDetailVO().getJobGroupName());

        Optional<JobVO> jobVO = jobService.findJob(savedJobWith1Trigger.getJobDetailVO().getJobName(), savedJobWith1Trigger.getJobDetailVO().getJobGroupName());
        assertThat(jobVO.isPresent(), is(false));
    }

    @Test
    public void should_pause_a_trigger() {
        JobVO job = aJobVO().jobDetail("test", TEST_JOB_GROUP, SimpleJob.class.getName(), "desc").end()
                .addTrigger("trigger31", "triggerGroup3").time(today(), tomorrow()).repeat(milliSeconds(100), 10).end()
                .build();
        jobService.createJob(job);

        final Date currentDate = executeInFuture(new Task() {
            @Override
            public void run() {
                jobService.pasuseTrigger("trigger31", "triggerGroup3");
            }
        }, seconds(1));

        executeInFuture(new Task() {
            @Override
            public void run() {
                Optional<Date> latest = jobResults.latest();
                assertThat(latest.isPresent(), is(true));
                assertThat(String.format("Latest execution time: %1$tH:%1$tM:%1$tS.%1$tL, expected to be before: %2$tH:%2$tM:%2$tS.%2$tL", latest.get(), currentDate), !latest.get().after(currentDate), is(true));
            }
        }, seconds(1));
    }

    @Test
    public void should_resume_a_trigger() {
        JobVO job = aJobVO().jobDetail("test", TEST_JOB_GROUP, SimpleJob.class.getName(), "desc").end()
                .addTrigger("trigger31", "triggerGroup3").time(today(), tomorrow()).repeat(milliSeconds(100), 10).end()
                .build();
        jobService.createJob(job);
        jobService.pasuseTrigger("trigger31", "triggerGroup3");

        final Date currentDate = executeInFuture(new Task() {
            @Override
            public void run() {
                jobService.resumeTrigger("trigger31", "triggerGroup3");
            }
        }, seconds(1));

        executeInFuture(new Task() {
            @Override
            public void run() {
                Optional<Date> latest = jobResults.latest();
                assertThat(latest.isPresent(), is(true));
                assertThat(String.format("Latest execution time: %1$tH:%1$tM:%1$tS.%1$tL, expected to be after: %2$tH:%2$tM:%2$tS.%2$tL", latest.get(), currentDate), latest.get().after(currentDate), is(true));
            }
        }, seconds(1));
    }

    @Test
    public void should_delete_a_trigger() {
        jobService.deleteTrigger("trigger11", "triggerGroup1");

        Optional<JobVO> jobVO = jobService.findJob(savedJobWith1Trigger.getJobDetailVO().getJobName(), savedJobWith1Trigger.getJobDetailVO().getJobGroupName());
        assertThat(jobVO.isPresent(), is(true));
        assertThat(jobVO.get().getTriggers().isEmpty(), is(true));
    }

    private Date executeInFuture(Task task, long seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
        }
        task.run();
        return new Date();
    }

    @Test
    public void should_find_all_jobs() {
        List<JobVO> allJobs = jobService.findAllJobs();
        assertThat(allJobs.size(), greaterThanOrEqualTo(1));
    }

    private void cleanAllJobs() throws SchedulerException {
        Set<JobKey> jobKeys = jobService.getScheduler().getJobKeys(GroupMatcher.<JobKey>groupEquals(TEST_JOB_GROUP));
        jobService.getScheduler().deleteJobs(ImmutableList.copyOf(jobKeys));
    }

    public static class DummyJob implements Job {
        private static int count = 0;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println(String.format("Dummy job is executed for %d times", ++count));
        }
    }

    public static class SimpleJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            jobResults.add(new Date());
        }
    }

    public static class JobResults {

        private List<Date> dates = Lists.newArrayList();

        public void add(Date date) {
            this.dates.add(date);
        }

        public Iterable<Date> laterThan(final Date current) {
            return Iterables.filter(dates, new Predicate<Date>() {
                @Override
                public boolean apply(@Nullable Date input) {
                    return input.after(current);
                }
            });
        }

        public <T> Optional<T> latest() {
            return (Optional<T>) (dates.isEmpty() ? Optional.absent() : Optional.of(dates.get(dates.size() - 1)));
        }
    }

    private static interface Task {
        public void run();
    }
}
