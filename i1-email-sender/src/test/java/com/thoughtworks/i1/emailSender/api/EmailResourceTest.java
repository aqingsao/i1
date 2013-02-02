package com.thoughtworks.i1.emailSender.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailResourceTest {
    @Test
    public void test_send_email() {
        String url = "http://localhost:8051/emailSender/api/email";
        WebResource webResource = Client.create().resource(url);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }
}
