package com.thoughtworks.i1.quartz.api;

import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.service.JobsService;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("jobs")
public class JobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResource.class);
    private JobsService jobsService;

    @Inject
    public JobsResource(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List getServers() {
        return jobsService.findAllJobs();
    }

    @POST
    @Path("schedule-json")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveSchedule(JobVO jobVo) {
        try{
            for(Trigger trigger : jobVo.getTriggers()){
                jobsService.scheduleJob(jobVo.getJobDetail(), trigger);
            }
        } catch (Exception e){
            LOGGER.error("save job failed!");
        }
    }
}
