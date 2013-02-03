package com.thoughtworks.i1.emailSender.api;

import com.google.common.base.Strings;
import com.sun.jersey.api.core.InjectParam;
import com.thoughtworks.i1.emailSender.domain.Address;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.domain.Sender;
import com.thoughtworks.i1.emailSender.domain.SendingEmailError;
import com.thoughtworks.i1.emailSender.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("email")
public class EmailResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailResource.class);

    @InjectParam
    private EmailService emailService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(Email email) {
        if (email.getSender() == null || email.getSender().getFrom() == null) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(withError("Missing from user")).build();
        }
        if (Strings.isNullOrEmpty(email.getSubject())) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(withError("Missing mail subject")).build();
        }
        if (Strings.isNullOrEmpty(email.getMessage())) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(withError("Missing mail body")).build();
        }
        boolean result = emailService.sendEmail(email);
        if (result) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private SendingEmailError withError(String message) {
        return new SendingEmailError(message);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Email getEmail() {
        return Email.anEmail(Address.anAddress("a@b.com"), "subject", "body", Address.anAddress("b@c.com"));
    }
}
