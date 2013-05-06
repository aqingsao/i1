package com.thoughtworks.i1.quartz.api;

import com.thoughtworks.i1.quartz.domain.QuartzVO;
import com.thoughtworks.i1.quartz.service.JobsService;
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
    private JobsService jobsService;
    @Context
    private UriInfo context;

    @Inject
    public JobsResource(JobsService jobsService) {
        this.jobsService = jobsService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("items")
    public List<QuartzVO> getQuartzJobs() {
        return jobsService.findAllJobs();
    }

    @POST
    @Path("item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSchedule(QuartzVO quartzVO) {
        try {
            jobsService.saveJob(quartzVO);
            List<QuartzVO> quartzVOs = getQuartzJobs();
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class)
                    .path("items");
            Response.ResponseBuilder builder = Response.created(path
                    .build());
            builder.contentLocation(path.build()).entity(quartzVOs);
            return (null == builder ? Response.noContent().build() : builder.build());


        } catch (Exception e) {
            LOGGER.error("save job failed!");
            return Response.serverError().build();
        }
    }

    @GET
    @Path("pause-trigger/{trrggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pauseSchedule(@PathParam("trrggerName")String triggerName,@PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobsService.pasuseTrigger(triggerName,triggerGroupName);

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


    @POST
    @Path("delete-trigger/{trrggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteSchedule(@PathParam("trrggerName")String triggerName,@PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobsService.deleteTrigger(triggerName,triggerGroupName);
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class)
                    .path("delete-trigger/{trrggerName}/{triggerGroupName");
            Response.ResponseBuilder builder = Response.created(path
                    .build());
            builder.contentLocation(path.build());
            return (null == builder ? Response.noContent().build() : builder.build());
        } catch (Exception e) {
            LOGGER.error("delete trigger faild!");
            return Response.serverError().build();
        }
    }

    @POST
    @Path("resume-trigger/{trrggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resumeSchedule(@PathParam("trrggerName")String triggerName,@PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobsService.resumeTrigger(triggerName,triggerGroupName);
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class)
                    .path("resume-trigger/{trrggerName}/{triggerGroupName}");
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
