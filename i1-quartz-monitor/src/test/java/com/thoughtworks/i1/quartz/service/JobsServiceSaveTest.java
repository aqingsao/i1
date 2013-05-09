package com.thoughtworks.i1.quartz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.commons.test.TransactionalDomainTestRunner;
import com.thoughtworks.i1.quartz.QuartzApplication;
import com.thoughtworks.i1.quartz.api.QuartzTestApplication;
import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.domain.QuartzVO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

@RunWith(TransactionalDomainTestRunner.class)
@RunWithApplication(QuartzTestApplication.class)
@Ignore
public class JobsServiceSaveTest {
    @Inject
    private JobsService jobsService;

    @Test
    public void should_() throws Exception {
        String  data = getData();

        QuartzVO quartzVO = (QuartzVO)jsonToBean(data, QuartzVO.class);

        jobsService.saveJob(quartzVO);

        List<JobVO> jobVOs = jobsService.getJobVOs();
        assertThat(jobVOs.size(), is(1));
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new QuartzApplication.QuartzModule());
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
        return "" +
                "    {\n" +
                "        \"jobName\": \"jobName\",\n" +
                "        \"jobGroupName\": \"jobGroupName\",\n" +
                "        \"description\": null,\n" +
                "        \"jobClass\": \"com.thoughtworks.i1.quartz.jobs.JobForTest\",\n" +
                "        \"jobDatas\": [\n" +
                "            {\n" +
                "                \"key\": \"url\",\n" +
                "                \"value\": \"test-url\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"triggers\": [\n" +
                "            {\n" +
                "                \"triggerName\": \"triggerName\",\n" +
                "                \"triggerGroupName\": \"triggerGroupName\",\n" +
                "                \"startTime\": 1367567457000,\n" +
                "                \"endTime\": null,\n" +
                "                \"triggerState\": null,\n" +
                "                \"repeatCount\": 0,\n" +
                "                \"repeatInterval\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"triggerName\": \"triggerName2\",\n" +
                "                \"triggerGroupName\": \"triggerGroupName\",\n" +
                "                \"startTime\": 1367567457000,\n" +
                "                \"endTime\": null,\n" +
                "                \"triggerState\": null,\n" +
                "                \"repeatCount\": 0,\n" +
                "                \"repeatInterval\": 0\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "" ;
    }
}
