package com.thoughtworks.i1.quartz.api;

import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.service.JobService;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Path("quartz-jobs")
public class JobsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResource.class);
    private JobService jobService;
    @Context
    private UriInfo context;

    @Inject
    public JobsResource(JobService jobService) {
        this.jobService = jobService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("items")
    public List<QuartzVO> getQuartzJobs() {
        return jobService.findAllJobs();
    }

    @POST
    @Path("item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSchedule(QuartzVO quartzVO) {
        try {
            jobService.saveJob(quartzVO);
            List<QuartzVO> quartzVOs = getQuartzJobs();
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class).path("items");
            Response.ResponseBuilder builder = Response.created(path.build());
            builder.contentLocation(path.build()).entity(quartzVOs);
            return builder.build();
        } catch (Exception e) {
            LOGGER.error("save job failed!");
            return Response.serverError().build();
        }
    }

    @GET
    @Path("pause-trigger/{triggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pauseSchedule(@PathParam("triggerName") String triggerName, @PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobService.pasuseTrigger(triggerName, triggerGroupName);

            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class)
                    .path("pause-trigger/{trrggerName}/{triggerGroupName}");
            Response.ResponseBuilder builder = Response.created(path
                    .build());
            builder.contentLocation(path.build());
            return (null == builder ? Response.noContent().build() : builder.build());
        } catch (Exception e) {
            LOGGER.error("pause trigger faild!");
            return Response.serverError().build();
        }
    }


    @GET
    @Path("delete-trigger/{triggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteSchedule(@PathParam("triggerName") String triggerName, @PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobService.deleteTrigger(triggerName, triggerGroupName);
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class)
                    .path("delete-trigger/{triggerName}/{triggerGroupName");
            Response.ResponseBuilder builder = Response.created(path
                    .build());
            builder.contentLocation(path.build());
            return (null == builder ? Response.noContent().build() : builder.build());
        } catch (Exception e) {
            LOGGER.error("delete trigger faild!");
            return Response.serverError().build();
        }
    }

    @GET
    @Path("resume-trigger/{triggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resumeSchedule(@PathParam("triggerName") String triggerName, @PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobService.resumeTrigger(triggerName, triggerGroupName);
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class)
                    .path("resume-trigger/{triggerName}/{triggerGroupName}");
            Response.ResponseBuilder builder = Response.created(path
                    .build());
            builder.contentLocation(path.build());
            return (null == builder ? Response.noContent().build() : builder.build());
        } catch (Exception e) {
            LOGGER.error("resume trigger faild!");
            return Response.serverError().build();
        }
    }


}
