package com.thoughtworks.i1.emailSender.api;

import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.domain.SendingEmailError;
import com.thoughtworks.i1.emailSender.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("email")
public class EmailResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailResource.class);

    private EmailService emailService;

    @Inject
    public EmailResource(EmailService emailService) {
        this.emailService = emailService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(Email email) {
        try {
            email.validate();

            emailService.sendEmail(email);
            return Response.ok().build();
        } catch (Exception e) {
            LOGGER.warn("Failed to send email: " + e.getMessage(), e);
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(new SendingEmailError(e.getMessage())).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Email> getEmail() {
        return emailService.findEmails();
    }
}
