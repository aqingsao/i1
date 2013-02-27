package com.thoughtworks.i1.emailSender.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.i1.emailSender.commons.JsonUtils;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.domain.SendingEmailError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static com.thoughtworks.i1.emailSender.domain.Address.anAddress;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailResourceTest extends AbstractResourceTest {

    @BeforeClass
    public static void beforeClass() throws Exception {
        server.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    @Test
    public void test_send_email_successfully() throws IOException {
        Email email = Email.anEmail(anAddress("i1_test@163.com"), "subject", "message", anAddress("i1_test@qq.com"));
        System.out.println(JsonUtils.toJson(email));

//        WebResource webResource = Client.create().resource(uri("/api/email"));
//        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);
//
//        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    @Test
    public void test_failed_to_send_email_when_from_user_is_null() throws IOException {
        WebResource webResource = Client.create().resource(uri("/api/email"));
        Email email = Email.anEmail(anAddress(""), "subject", "message", anAddress("b@c.com"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Invalid from address"));
    }

    @Test
    public void test_failed_to_send_email_when_to_user_is_null() throws IOException {
        WebResource webResource = Client.create().resource(uri("/api/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "message", anAddress(""));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Invalid to address"));
    }

    @Test
    public void test_failed_to_send_email_when_subject_is_empty() throws IOException {
        WebResource webResource = Client.create().resource(uri("/api/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "", "body", anAddress("b@c.com"));
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Missing mail subject"));
    }

    @Test
    public void test_failed_to_send_email_when_message_is_empty() throws IOException {
        WebResource webResource = Client.create().resource(uri("/api/email"));
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "", anAddress("b@c.com"));

        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Missing mail body"));
    }

    @Test
    public void test_should_return_empty_emails_when_no_mails_are_sent() {
        removeAll(Email.class);

        ClientResponse clientResponse = Client.create().resource(uri("/api/email")).get(ClientResponse.class);

        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        List<Email> emails = clientResponse.getEntity(List.class);
        assertThat(emails.isEmpty(), is(true));
    }

    @Test
    public void test_should_return_1_email_when_only_1_mail_is_sent() {
        removeAll(Email.class);

        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "", anAddress("b@c.com"));
        persist(email);

        ClientResponse clientResponse = Client.create().resource(uri("/api/email")).get(ClientResponse.class);

        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        List<Email> emails = clientResponse.getEntity(new GenericType<List<Email>>(){
        });
        assertThat(emails.size(), is(1));
        Email actual = emails.get(0);
        assertThat(actual.getSender().getFrom().getUserAddress(), is("a@b.com"));
        assertThat(actual.getSubject(), is(email.getSubject()));
        assertThat(actual.getRecipients().getToAddresses().get(0).getUserAddress(), is("b@c.com"));
        assertThat(actual.getMessage(), is(""));
    }
}
