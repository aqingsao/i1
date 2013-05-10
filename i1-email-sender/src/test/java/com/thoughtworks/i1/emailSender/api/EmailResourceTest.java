package com.thoughtworks.i1.emailSender.api;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.thoughtworks.i1.commons.test.AbstractResourceTest;
import com.thoughtworks.i1.commons.test.ApiTestRunner;
import com.thoughtworks.i1.commons.test.I1TestApplication;
import com.thoughtworks.i1.commons.test.RunWithApplication;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.domain.SendingEmailError;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static com.thoughtworks.i1.emailSender.domain.Address.anAddress;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(ApiTestRunner.class)
@RunWithApplication(EmailTestApplication.class)
@Ignore
public class EmailResourceTest extends AbstractResourceTest {

    @Test
    public void test_send_email_successfully() throws IOException {
        Email email = Email.anEmail(anAddress("i1_test@163.com"), "subject", "message", anAddress("i1_test@qq.com"));
        ClientResponse response = post("/api/email", email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.OK));
    }

    @Test
    public void test_failed_to_send_email_when_from_user_is_null() throws IOException {
        Email email = Email.anEmail(anAddress(""), "subject", "message", anAddress("b@c.com"));
        ClientResponse response = post("/api/email", email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Invalid from address"));
    }

    @Test
    public void test_failed_to_send_email_when_to_user_is_null() throws IOException {
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "message", anAddress(""));
        ClientResponse response = post("/api/email", email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Invalid to address"));
    }

    @Test
    public void test_failed_to_send_email_when_subject_is_empty() throws IOException {
        Email email = Email.anEmail(anAddress("a@b.com"), "", "body", anAddress("b@c.com"));
        ClientResponse response = post("/api/email", email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Missing mail subject"));
    }

    @Test
    public void test_failed_to_send_email_when_message_is_empty() throws IOException {
        Email email = Email.anEmail(anAddress("a@b.com"), "subject", "", anAddress("b@c.com"));
        ClientResponse response = post("/api/email", email);

        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.NOT_ACCEPTABLE));
        SendingEmailError entity = response.getEntity(SendingEmailError.class);
        assertThat(entity.getMessage(), is("Missing mail body"));
    }

    @Test
    public void test_should_return_empty_emails_when_no_mails_are_sent() {
        removeAll(Email.class);

        ClientResponse clientResponse = get("/api/email");

        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        List<Email> emails = clientResponse.getEntity(List.class);
        assertThat(emails.isEmpty(), is(true));
    }

    @Test
    public void test_should_return_1_email_when_only_1_mail_is_sent() {
        removeAll(Email.class);
        Email email = persist(Email.anEmail(anAddress("a@b.com"), "subject", "", anAddress("b@c.com")));

        ClientResponse clientResponse = get("/api/email");

        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        List<Email> emails = clientResponse.getEntity(new GenericType<List<Email>>() {
        });
        assertThat(emails.size(), is(1));
        Email actual = emails.get(0);
        assertThat(actual.getSender().getFrom().getUserAddress(), is("a@b.com"));
        assertThat(actual.getSubject(), is(email.getSubject()));
        assertThat(actual.getRecipients().getToAddresses().get(0).getUserAddress(), is("b@c.com"));
        assertThat(actual.getMessage(), is(""));
    }
}
