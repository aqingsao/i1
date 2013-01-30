package com.thoughtworks.ms.email;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.thoughtworks.ms.email.model.Email;
import org.junit.Test;

import javax.mail.MessagingException;

import static com.thoughtworks.ms.email.model.Address.anAddress;
import static com.thoughtworks.ms.email.model.Email.anEmail;
import static com.thoughtworks.ms.email.model.Sender.aSender;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailServiceTest {

    private EmailService emailService;
    private EmailConfiguration emailConfiguration;

    @Test
    public void should_send_email_with_163_as_smtp_server() {
        emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailServerHost("smtp.163.com");

        emailConfiguration.setAuthenticationUserName("vinci_zhang");
        emailConfiguration.setAuthenticationPassword("j1ngn0ng");
        emailConfiguration.setAuthenticationNeeded(true);
        emailService = new EmailService(emailConfiguration);

        boolean result = emailService.sendEmail(anEmail(anAddress("Admin", "vinci_zhang@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "vinci.zhang@gmail.com")));
        assertThat(result, is(true));
    }

    @Test
    public void test_send_email_with_1_attachment() {
        emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailServerHost("smtp.163.com");

        emailConfiguration.setAuthenticationUserName("vinci_zhang");
        emailConfiguration.setAuthenticationPassword("j1ngn0ng");
        emailConfiguration.setAuthenticationNeeded(true);

        emailService = new EmailService(emailConfiguration);

        String attachment = this.getClass().getClassLoader().getResource("attachment1.txt").getFile();
        Email email = anEmail(anAddress("Admin", "vinci_zhang@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "vinci.zhang@gmail.com"), attachment);
        boolean result = emailService.sendEmail(email);
        assertThat(result, is(true));
    }

    @Test
    public void test_() throws MessagingException, UserException {
        GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);
        GreenMailUser user = mailServer.setUser("admin@test.com", "123456");
        mailServer.start();

        Email email = anEmail(anAddress("Admin", "vinci_zhang@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "vinci.zhang@gmail.com"));

        emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailServerPort(ServerSetupTest.SMTP.getPort());
        emailService = new EmailService(emailConfiguration);
        emailService.sendEmail(email);

        assertEquals(2, mailServer.getReceivedMessages().length);

        mailServer.stop();
    }
}