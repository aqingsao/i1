package com.thoughtworks.i1.quartz.jobs;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.ws.rs.core.MediaType;

public class JobForUrl implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String url = jobDataMap.getString("url");
        this.willBeExecuteMethod(url);
    }

    protected void willBeExecuteMethod(String url) {
        System.out.println("-------url="+url);
        System.out.println("-------into-willBeExecuteMethod----");
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        Client client = Client.create(clientConfig);
        ClientResponse clientResponse = client.resource(url)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println("---execute-over---------");
        int a = clientResponse.getStatus();
        System.out.println("--------------" + a);
    }
}
