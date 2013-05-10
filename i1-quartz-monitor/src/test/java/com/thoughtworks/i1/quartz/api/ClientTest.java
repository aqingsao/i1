package com.thoughtworks.i1.quartz.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.domain.TriggerVO;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.thoughtworks.i1.quartz.domain.QuartzVO.QuartzVOBuilder.aQuartzVO;

public class ClientTest {
    public static void main(String[] args) {
        QuartzVO quartzVO = aQuartzVO().jobDetail("b", "herenSchedule", "com.thoughtworks.i1.quartz.jobs.JobForUrl")
                .addJobData("url", "http://localhost:8051/heren/api/diagnosis-clinic-dict/test").end()
                .addTrigger("a", "herenTrigger").time(new Date(), new Date()).repeat(7, 9).end()
                .build();

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
