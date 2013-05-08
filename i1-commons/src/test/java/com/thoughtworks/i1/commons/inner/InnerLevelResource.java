package com.thoughtworks.i1.commons.inner;

import com.google.inject.name.Named;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/inner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InnerLevelResource {
    private String property;

//    public InnerLevelResource(@Named("foo") String property){
//        this.property = property;
//    }
    @GET
    public Response get() {
        return Response.ok().entity("inner").build();
    }

    @GET
    @Path("property")
    public Response getProperty(){
        return Response.ok().entity(property).build();
    }
}
