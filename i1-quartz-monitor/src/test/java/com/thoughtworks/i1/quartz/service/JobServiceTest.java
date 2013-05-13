package com.thoughtworks.i1.quartz.service;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.commons.test.TransactionalDomainTestRunner;
import com.thoughtworks.i1.quartz.api.QuartzTestApplication;
import com.thoughtworks.i1.quartz.domain.JobVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.thoughtworks.i1.commons.util.DateHelper.*;
import static com.thoughtworks.i1.quartz.domain.JobVO.QuartzVOBuilder.aJobVO;
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

    @Before
    public void before() {
        savedJobWith1Trigger = aJobVO().jobDetail("name1", TEST_JOB_GROUP, "com.thoughtworks.i1.quartz.DummyJob").end()
                .addTrigger("trigger11", "triggerGroup1").time(today(), tomorrow()).repeat(minutes(1), 1).end()
                .build();
        jobWith2Triggers = aJobVO().jobDetail("name2", TEST_JOB_GROUP, "com.thoughtworks.i1.quartz.DummyJob").end()
                .addTrigger("trigger21", "triggerGroup2").time(today(), tomorrow()).repeat(minutes(1), 1).end()
                .addTrigger("trigger22", "triggerGroup2").time(today(), tomorrow()).repeat(minutes(2), 1).end()
                .build();
        jobService.saveJob(savedJobWith1Trigger);
    }

    @After
    public void after() throws SchedulerException {
        Set<JobKey> jobKeys = jobService.getScheduler().getJobKeys(GroupMatcher.<JobKey>groupEquals(TEST_JOB_GROUP));
        jobService.getScheduler().deleteJobs(ImmutableList.copyOf(jobKeys));
    }

    @Test
    public void should_save_job_successfully() {
        jobService.saveJob(jobWith2Triggers);

        List<JobVO> allJobs = jobService.findAllJobs();
        Optional<JobVO> jobVO = getJobVO(allJobs, jobWith2Triggers.getJobDetailVO().getJobName(), jobWith2Triggers.getJobDetailVO().getJobGroupName());
        assertThat(jobVO.isPresent(), is(true));
        assertThat(jobVO.get().getJobClass(), is("com.thoughtworks.i1.quartz.DummyJob"));
    }

    @Test
    public void should_throw_exception_when_job_name_and_group_name_already_exists() {
        try {
            jobService.saveJob(savedJobWith1Trigger);
            fail("Should fail as job is already saved in before() method");
        } catch (SystemException e) {
            assertThat(e.getMessage().startsWith("Failed to save job"), is(true));
        }
    }

    @Test
    public void should_throw_exception_when_job_is_already_saved() throws Exception {
        JobVO jobWithDuplicateName = aJobVO().jobDetail(savedJobWith1Trigger.getJobDetailVO().getJobName(), savedJobWith1Trigger.getJobDetailVO().getJobGroupName(), "com.thoughtworks.i1.quartz.DummyJob").end()
                .addTrigger("trigger31", "triggerGroup3").time(today(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        try {
            jobService.saveJob(jobWithDuplicateName);
            fail("Should fail as job name and group name is duplicate with existing ones");
        } catch (SystemException e) {
            assertThat(e.getMessage().startsWith("Failed to save job"), is(true));
        }
    }

    @Test
    public void should_throw_exception_when_job_class_name_cannot_be_found() throws Exception {
        JobVO jobWithDuplicateName = aJobVO().jobDetail("test", TEST_JOB_GROUP, "job.class.undefined").end()
                .addTrigger("trigger31", "triggerGroup3").time(today(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        try {
            jobService.saveJob(jobWithDuplicateName);
            fail("Should fail as job name cannot be found");
        } catch (SystemException e) {
            assertThat(e.getMessage().startsWith("Cannot find job with name"), is(true));
        }
    }

    private Optional<JobVO> getJobVO(List<JobVO> jobVOs, String name, String group) {
        for (JobVO jobVO : jobVOs) {
            if (jobVO.getJobName().equals(name) && jobVO.getJobGroupName().equals(group)) {
                return Optional.of(jobVO);
            }
        }
        return Optional.absent();
    }

    @Test
    public void should_delete_a_job() {

    }
}
