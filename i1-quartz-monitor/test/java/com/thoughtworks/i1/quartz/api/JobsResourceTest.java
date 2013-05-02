package com.thoughtworks.i1.quartz.api;

import com.sun.jersey.api.client.ClientResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JobsResourceTest extends AbstractResourceTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
        server.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    @Test
    public void should_() {
        ClientResponse response = get("/api/jobs");

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }
}
