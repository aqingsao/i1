package com.thoughtworks.i1.quartz.jobs;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.thoughtworks.i1.commons.Errors;
import com.thoughtworks.i1.quartz.domain.QrtzHistory;
import com.thoughtworks.i1.quartz.service.QrtzHistoryService;
import org.quartz.*;

import javax.ws.rs.core.MediaType;
import java.util.Date;

@DisallowConcurrentExecution
public class JobForUrl implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long startTime = new Date().getTime();
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String url = jobDataMap.getString("url");
        ClientResponse clientResponse = this.willBeExecuteMethod(url);

        int isNormal = 1;
        String exceptionDesc = "";
        int status = clientResponse.getStatus();
        if(status >= 400){
            isNormal = 0;
            Errors errors = clientResponse.getEntity(Errors.class);
            exceptionDesc = errors.toString();
        }

        Trigger trigger = context.getTrigger();
        Long endTime =  new Date().getTime();
        try{
            QrtzHistory qrtzHistory = new QrtzHistory(context.getScheduler().getSchedulerName(),
                    trigger.getKey().getName(),
                    trigger.getKey().getGroup(),
                    trigger.getJobKey().getName(),
                    trigger.getJobKey().getGroup(),
                    startTime,
                    endTime,
                    isNormal,
                    exceptionDesc);

            QrtzHistoryService qrtzHistoryService = new QrtzHistoryService();
            qrtzHistoryService.insertQrtzHistory(qrtzHistory);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    protected ClientResponse willBeExecuteMethod(String url) {

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        Client client = Client.create(clientConfig);
        return client.resource(url)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);


    }
}
