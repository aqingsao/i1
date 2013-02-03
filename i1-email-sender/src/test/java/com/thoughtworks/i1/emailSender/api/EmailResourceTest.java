package com.thoughtworks.i1.emailSender.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.domain.SendingEmailError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static com.thoughtworks.i1.emailSender.domain.Address.anAddress;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailResourceTest extends AbstractResourceTest {

    @BeforeClass
    public static void beforeClass() throws IOException {
        httpServer.start();
    }

    @AfterClass
    public static void afterClass() {
        httpServer.stop();
    }

    @Ignore("Not implemented yet")
    public void test_send_email_successfully() throws IOException {
        WebResource webResource = Client.create().resource(uri("/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "message", anAddress("b@c.com"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    @Test
    public void test_failed_to_send_email_when_from_user_is_null() throws IOException {
        WebResource webResource = Client.create().resource(uri("/email"));
        Email email = Email.anEmail(anAddress(""), "subject", "message", anAddress("b@c.com"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Invalid from address"));
    }

    @Test
    public void test_failed_to_send_email_when_to_user_is_null() throws IOException {
        WebResource webResource = Client.create().resource(uri("/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "message", anAddress(""));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Invalid to address"));
    }

    @Test
    public void test_failed_to_send_email_when_subject_is_empty() throws IOException {
        WebResource webResource = Client.create().resource(uri("/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "", "body", anAddress("b@c.com"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Missing mail subject"));
    }

    @Test
    public void test_failed_to_send_email_when_message_is_empty() throws IOException {
        WebResource webResource = Client.create().resource(uri("/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "", anAddress("b@c.com"));

        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Missing mail body"));
    }
}
