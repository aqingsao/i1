package com.thoughtworks.i1.quartz.api;

import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.i1.commons.test.AbstractResourceTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(QuartzApiTestRunner.class)
public class JobsResourceTest extends AbstractResourceTest {

    @Test
    public void should_() {
        ClientResponse response = get("/api/quartz-jobs/items");

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }
}
