package com.thoughtworks.i1.quartz.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.thoughtworks.i1.commons.test.AbstractResourceTest;
import com.thoughtworks.i1.commons.test.ApiTestRunner;
import com.thoughtworks.i1.commons.test.I1TestApplication;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.quartz.domain.JobDataVO;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(ApiTestRunner.class)
@RunWithApplication(QuartzTestApplication.class)
public class JobsResourceTest  extends AbstractResourceTest {

    @Test
    public void should_() {
        ClientResponse response = get("/api/quartz-jobs/items");

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    @Test
    public void should_invoke_url(){

        List<TriggerVO> triggerVOList = new ArrayList<>();
        TriggerVO triggerVO = new TriggerVO();
        triggerVO.setTriggerName("a");
        triggerVO.setTriggerGroupName("aaa");
        triggerVO.setTriggerState("default");
        triggerVO.setStartTime(new Date());
        triggerVO.setEndTime(new Date());
        triggerVO.setRepeatCount(9);
        triggerVO.setRepeatInterval(7);
        triggerVOList.add(triggerVO);

        List<JobDataVO> jobDataVOList = new ArrayList<>();
        JobDataVO jobDataVO = new JobDataVO();
        jobDataVO.setKey("url");
        jobDataVO.setValue("http://localhost:8051/heren/api/diagnosis-clinic-dict/test");
        jobDataVOList.add(jobDataVO);

        QuartzVO quartzVO = new QuartzVO();
        quartzVO.setJobName("aa");
        quartzVO.setJobGroupName("herenSchedule");
        quartzVO.setDescription("use schedule");
        quartzVO.setJobClass("com.thoughtworks.i1.quartz.jobs.JobForUrl");
        quartzVO.setTriggers(triggerVOList);
        quartzVO.setJobDatas(jobDataVOList);

        ClientResponse clientResponse = get("/api/quartz-jobs/items");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }
}
