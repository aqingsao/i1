package com.thoughtworks.i1.quartz.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.thoughtworks.i1.quartz.domain.JobDataVO;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientTest {
    public static void main(String[] args) {
        List<TriggerVO> triggerVOList = new ArrayList<>();
        TriggerVO triggerVO = new TriggerVO();
        triggerVO.setTriggerName("a");
        triggerVO.setTriggerGroupName("herenTrigger");
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
        quartzVO.setJobName("b");
        quartzVO.setJobGroupName("herenSchedule");
        quartzVO.setDescription("use schedule");
        quartzVO.setJobClass("com.thoughtworks.i1.quartz.jobs.JobForUrl");
        quartzVO.setTriggers(triggerVOList);
        quartzVO.setJobDatas(jobDataVOList);

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        Client client = Client.create(clientConfig);
        ClientResponse clientResponse = client.resource("http://localhost:8051/schedule/api/quartz-jobs/item")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        int a = clientResponse.getStatus();

        System.out.println(a);
    }
}
