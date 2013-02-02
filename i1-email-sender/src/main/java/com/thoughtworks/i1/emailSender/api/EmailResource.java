package com.thoughtworks.i1.emailSender.api;

import com.thoughtworks.i1.emailSender.domain.Address;
import com.thoughtworks.i1.emailSender.domain.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("email")
public class EmailResource {
    private static final Logger logger= LoggerFactory.getLogger(EmailResource.class);

    @POST
    public void sendEmail(){

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Email getEmail(){
        return Email.anEmail(Address.anAddress("a@b.com"), "subject", "body", Address.anAddress("b@c.com"));
    }
}
