package com.thoughtworks.i1.quartz.api;

import com.thoughtworks.i1.quartz.domain.JobVO;
import com.thoughtworks.i1.quartz.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

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
    public List<JobVO> getQuartzJobs() {
        return jobService.findAllJobs();
    }

    @POST
    @Path("item")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createJob(JobVO jobVO) {
        try {
            jobVO = jobService.createJob(jobVO);
            URI path = context.getBaseUriBuilder().path(JobsResource.class).path("items").build();
            return Response.created(path).entity(jobVO).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("{jobName}/{jobGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteJob(@PathParam("jobName") String jobName, @PathParam("jobGroupName") String jobGroupName) {
        try {
            jobService.deleteJob(jobName, jobGroupName);
            return Response.ok().build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }


    @GET
    @Path("delete-trigger/{triggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteSchedule(@PathParam("triggerName") String triggerName, @PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobService.deleteTrigger(triggerName, triggerGroupName);
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class).path("delete-trigger/{triggerName}/{triggerGroupName");
            return Response.created(path.build()).contentLocation(path.build()).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("resume-trigger/{triggerName}/{triggerGroupName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resumeSchedule(@PathParam("triggerName") String triggerName, @PathParam("triggerGroupName") String triggerGroupName) {
        try {
            jobService.resumeTrigger(triggerName, triggerGroupName);
            UriBuilder path = context.getBaseUriBuilder().path(JobsResource.class).path("resume-trigger/{triggerName}/{triggerGroupName}");
            return Response.created(path.build()).contentLocation(path.build()).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.serverError().build();
        }
    }
}
