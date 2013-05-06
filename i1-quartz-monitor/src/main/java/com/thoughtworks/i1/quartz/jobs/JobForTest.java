package com.thoughtworks.i1.quartz.jobs;

import org.quartz.*;

import java.util.Date;

@DisallowConcurrentExecution
public class JobForTest implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("JobForTest begin" + new Date());
//        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
//        String url = jobDataMap.getString("url");
//        System.out.println("JobForTest url = " + url);

        Date date = new Date();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String url = jobDataMap.getString("url");
        try{
            Thread.currentThread().sleep(5000);
        } catch(InterruptedException ie){
            ie.printStackTrace();
        }
        System.out.println("JobForTest url = " + url + ", begin :" + date);

    }

}
