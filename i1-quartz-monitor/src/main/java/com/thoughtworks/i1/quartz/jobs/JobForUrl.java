package com.thoughtworks.i1.quartz.jobs;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
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
        this.willBeExecuteMethod(url);

        Trigger trigger = context.getTrigger();
        Long endTime =  new Date().getTime();
        try{
            QrtzHistory qrtzHistory = new QrtzHistory(context.getScheduler().getSchedulerInstanceId(),
                    trigger.getKey().getName(),
                    trigger.getKey().getGroup(),
                    trigger.getJobKey().getName(),
                    trigger.getJobKey().getGroup(),
                    startTime,
                    endTime,
                    1,
                    "");

            QrtzHistoryService qrtzHistoryService = new QrtzHistoryService();
            qrtzHistoryService.insertQrtzHistory(qrtzHistory);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    protected void willBeExecuteMethod(String url) {
        System.out.println("-------------------------------------------------------url="+url);
//        System.out.println("-------into-willBeExecuteMethod----");
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);
        Client client = Client.create(clientConfig);
        ClientResponse clientResponse = client.resource(url)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        Date date = new Date();
        System.out.println("JobForTest url = " + url + ", begin :" +    date) ;
//        System.out.println("---execute-over---------");
        int a = clientResponse.getStatus();
        System.out.println("------------------------------------------------------" + a);
    }
}
