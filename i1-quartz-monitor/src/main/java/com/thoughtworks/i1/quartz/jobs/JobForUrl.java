package com.thoughtworks.i1.quartz.jobs;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.deploy.util.SessionState;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.ws.rs.core.MediaType;

public class JobForUrl implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {


    }

    protected void willBeExecuteMethod(String BASE_URI, String URI) {
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        SessionState.Client client = Client.create(clientConfig);
        ClientResponse clientResponse = client.resource(BASE_URI)
                .path(URI)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println("-------------");
        int a = clientResponse.getStatus();
        System.out.println(a);
    }
}
