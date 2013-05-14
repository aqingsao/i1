package com.thoughtworks.i1.quartz.api;

import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.i1.commons.test.AbstractResourceTest;
import com.thoughtworks.i1.commons.test.ApiTestRunner;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.service.JobServiceTest;
import org.eclipse.jetty.http.HttpHeader;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Date;
import java.util.List;

import static com.thoughtworks.i1.commons.util.DateHelper.minutes;
import static com.thoughtworks.i1.commons.util.DateHelper.tomorrow;
import static com.thoughtworks.i1.commons.util.DateHelper.yesterday;
import static com.thoughtworks.i1.quartz.domain.JobVO.QuartzVOBuilder.aJobVO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(ApiTestRunner.class)
@RunWithApplication(QuartzTestApplication.class)
public class JobsResourceTest extends AbstractResourceTest {

    @Test
    public void should_return_empty_job_items() {
        ClientResponse response = get("/api/quartz-jobs/items");

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    @Test
    public void should_save_job_successfully() {
        JobVO jobVO = aJobVO().jobDetail("jobName", "groupName", JobServiceTest.DummyJob.class.getName())
                .addJobData("url", "http://localhost:8051/heren/api/diagnosis-clinic-dict/test").end()
                .addTrigger("triggerName", "triggerGroupName").time(yesterday(), tomorrow()).repeat(minutes(10), 1).end()
                .build();
        ClientResponse response = post("/api/quartz-jobs/item", jobVO);
        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.CREATED));
        assertThat(getHeader(response, HttpHeaders.LOCATION).size(), is(1));
    }

    @Test
    public void should_invoke_url() {

//        List<TriggerVO> triggerVOList = new ArrayList<>();
//        TriggerVO triggerVO = new TriggerVO.TriggerVOBuilder(parent, triggerName, triggerGroupName).createTriggerVO();
//        triggerVO.setTriggerName("a");
//        triggerVO.setTriggerGroupName("aaa");
//        triggerVO.setTriggerState("default");
//        triggerVO.setStartTime(new Date());
//        triggerVO.setEndTime(new Date());
//        triggerVO.setRepeatCount(9);
//        triggerVO.setRepeatInterval(7);
//        triggerVOList.add(triggerVO);
//
//        List<JobDataVO> jobDataVOList = new ArrayList<>();
//        JobDataVO jobDataVO = new JobDataVO();
//        jobDataVO.setKey("url");
//        jobDataVO.setValue("http://localhost:8051/heren/api/diagnosis-clinic-dict/test");
//        jobDataVOList.add(jobDataVO);
//
//        JobVO jobVO = new JobVO.QuartzVOBuilder().createQuartzVO();
//        jobVO.setJobName("aa");
//        jobVO.setJobGroupName("herenSchedule");
//        jobVO.setDescription("use schedule");
//        jobVO.setJobClass("com.thoughtworks.i1.quartz.jobs.JobForUrl");
//        jobVO.setTriggers(triggerVOList);
//        jobVO.setJobDatas(jobDataVOList);

        JobVO jobVO = aJobVO().jobDetail("b", "herenSchedule", "com.thoughtworks.i1.quartz.jobs.JobForUrl")
                .addJobData("url", "http://localhost:8051/heren/api/diagnosis-clinic-dict/test").end()
                .addTrigger("a", "herenTrigger").time(new Date(), new Date()).repeat(7, 9).end()
                .build();

        ClientResponse clientResponse = get("/api/quartz-jobs/items");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }
}
