package com.thoughtworks.i1.emailSender.email;

import org.junit.Ignore;
import org.junit.Test;

import static com.thoughtworks.i1.emailSender.email.model.Address.anAddress;
import static com.thoughtworks.i1.emailSender.email.model.Email.anEmail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailServiceIntegrationTest {

    private EmailService emailService;
    private EmailConfiguration emailConfiguration;

    @Test
    public void should_send_email_with_163_as_smtp_server() {
        emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailServerHost("smtp.163.com");

        emailConfiguration.setAuthenticationUserName("i1_test");
        emailConfiguration.setAuthenticationPassword("ThoughtWorks");
        emailConfiguration.setAuthenticationNeeded(true);
        emailService = new EmailService(emailConfiguration);

        boolean result = emailService.sendEmail(anEmail(anAddress("Admin", "i1_test@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@qq.com")));
        assertThat(result, is(true));
    }

    @Ignore("Should setup qq mail to support smtp")
    public void should_send_email_with_qq_as_smtp_server() {
        emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailServerHost("smtp.qq.com");

        emailConfiguration.setAuthenticationUserName("i1_test");
        emailConfiguration.setAuthenticationPassword("ThoughtWorks");
        emailConfiguration.setAuthenticationNeeded(true);
        emailService = new EmailService(emailConfiguration);

        boolean result = emailService.sendEmail(anEmail(anAddress("Admin", "i1_test@qq.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@163.com")));
        assertThat(result, is(true));
    }
}