package com.thoughtworks.i1.quartz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobsServiceSaveTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new QuartzModule());
        JobsService jobsService = injector.getInstance(JobsService.class);
        try{

            String  data = getData();

            QuartzVO quartzVO = (QuartzVO)jsonToBean(data, QuartzVO.class);
            System.out.println(quartzVO.getJobName());
            jobsService.saveJob(quartzVO);


            sleep(20000);
            jobsService.shutdown();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static Object  jsonToBean(String json, Class<?> cls) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object vo = mapper.readValue(json, cls);
        return vo;
    }

    private static String getData(){
        return "    {\n" +
                "        \"jobName\": \"jobName\",\n" +
                "        \"jobGroupName\": \"jobGroupName\",\n" +
                "        \"jobClass\": \"com.thoughtworks.i1.quartz.jobs.JobForTest\",\n" +
                "        \"jobData\": {\n" +
                "            \"url\": \"test-url\"\n" +
                "        },\n" +
                "        \"triggers\": [\n" +
                "            {\n" +
                "                \"triggerName\": \"triggerName\",\n" +
                "                \"triggerGroupName\": \"triggerGroupName\",\n" +
                "                \"startTime\": 1367475197000,\n" +
                "                \"endTime\": null,\n" +
                "                \"triggerState\": null,\n" +
                "                \"repeatCount\": -1,\n" +
                "                \"repeatInterval\": 5000\n" +
                "            }\n" +
                "            ,{\n" +
                "                \"triggerName\": \"triggerName2\",\n" +
                "                \"triggerGroupName\": \"triggerGroupName\",\n" +
                "                \"startTime\": 1367475197000,\n" +
                "                \"endTime\": null,\n" +
                "                \"triggerState\": null,\n" +
                "                \"repeatCount\": -1,\n" +
                "                \"repeatInterval\": 3000\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" ;
    }
}
