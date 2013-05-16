package com.thoughtworks.i1.quartz.jobs;

import com.thoughtworks.i1.quartz.domain.QrtzHistory;
import com.thoughtworks.i1.quartz.service.QrtzHistoryService;
import org.quartz.*;

import java.util.Date;

@DisallowConcurrentExecution
public class JobForTest implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long startTime = new Date().getTime();


        Date date = new Date();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String url = jobDataMap.getString("url");

        System.out.println("-------------"+new Date());
        System.out.println("JobForTest url = " + url + ", begin :" + date);

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



}
