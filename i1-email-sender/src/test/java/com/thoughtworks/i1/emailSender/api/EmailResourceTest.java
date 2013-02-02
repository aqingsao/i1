package com.thoughtworks.i1.emailSender.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import java.io.IOException;
import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailResourceTest extends AbstractResourceTest {

    @Before
    public void before() throws IOException {
        httpServer.start();
    }

    @After
    public void after() {
        httpServer.stop();
    }

    @Test
    public void test_send_email() throws IOException {
        WebResource webResource = Client.create().resource(uri("/email"));
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
        String entity = response.getEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNodes = mapper.readTree(entity);
        assertThat(jsonNodes.has("subject"), is(true));
    }

}
