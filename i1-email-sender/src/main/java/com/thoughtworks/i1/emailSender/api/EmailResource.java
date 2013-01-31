package com.thoughtworks.i1.emailSender.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/email")
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {
    private static final Logger logger= LoggerFactory.getLogger(EmailResource.class);


}
