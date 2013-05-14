package com.thoughtworks.i1.quartz.api;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.i1.commons.test.AbstractResourceTest;
import com.thoughtworks.i1.commons.test.ApiTestRunner;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import com.thoughtworks.i1.quartz.service.JobServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import javax.ws.rs.core.HttpHeaders;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.thoughtworks.i1.commons.util.DateHelper.*;
import static com.thoughtworks.i1.quartz.domain.JobVO.QuartzVOBuilder.aJobVO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(ApiTestRunner.class)
@RunWithApplication(QuartzTestApplication.class)
public class JobsResourceTest extends AbstractResourceTest {
    @Inject
    private SchedulerFactory factory;
    private JobVO savedJob;

    @Before
    public void before() throws SchedulerException {
        savedJob = aJobVO().jobDetail("jobName", "groupName", JobServiceTest.DummyJob.class.getName())
                .addJobData("url", "http://localhost:8051/heren/api/diagnosis-clinic-dict/test").end()
                .addTrigger("triggerName", "triggerGroupName").time(yesterday(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        createJob(savedJob);

    }

    @After
    public void after() throws SchedulerException {
        Set<JobKey> jobKeys = factory.getScheduler().getJobKeys(GroupMatcher.<JobKey>groupEquals("groupName"));
        factory.getScheduler().deleteJobs(ImmutableList.copyOf(jobKeys));
    }

    @Test
    public void should_return_empty_job_items() {
        ClientResponse response = get("/api/quartz-jobs/items");
        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    @Test
    public void should_save_job_successfully() {
        JobVO jobVO = aJobVO().jobDetail("jobName1", "groupName1", JobServiceTest.DummyJob.class.getName())
                .addJobData("url", "http://localhost:8051/heren/api/diagnosis-clinic-dict/test").end()
                .addTrigger("triggerName1", "triggerGroupName1").time(yesterday(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        ClientResponse response = post("/api/quartz-jobs/item", jobVO);
        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.CREATED));
        assertThat(getHeader(response, HttpHeaders.LOCATION).size(), is(1));
    }

    @Test
    public void should_delete_job_successfully() {
        ClientResponse response = delete(String.format("/api/quartz-jobs/%s/%s", savedJob.getJobDetailVO().getJobName(), savedJob.getJobDetailVO().getJobGroupName()));
        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    private void createJob(JobVO jobVO) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        JobDetail jobDetail = jobVO.getJobDetail();
        scheduler.addJob(jobDetail, false);

        List<TriggerVO> triggerVOs = jobVO.getTriggers();
        for (TriggerVO triggerVO : triggerVOs) {
            scheduler.scheduleJob(triggerVO.toTrigger(jobDetail.getKey()));
        }
    }
}
