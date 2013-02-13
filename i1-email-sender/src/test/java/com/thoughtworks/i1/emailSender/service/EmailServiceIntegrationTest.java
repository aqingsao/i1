package com.thoughtworks.i1.emailSender.service;

import com.thoughtworks.i1.emailSender.commons.BusinessException;
import com.thoughtworks.i1.emailSender.commons.SystemException;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;

import static com.thoughtworks.i1.emailSender.domain.Address.anAddress;
import static com.thoughtworks.i1.emailSender.domain.Email.anEmail;

@Ignore
public class EmailServiceIntegrationTest {

    private EmailService emailService;
    private EmailConfiguration emailConfiguration;
    private EntityManager entityManager;

    @Test
    public void should_send_email_with_163_as_smtp_server() {
        emailService = new EmailService(new EmailConfiguration(25, "smtp.163.com", true, "i1_test", "ThoughtWorks"), entityManager);

        emailService.sendEmail(anEmail(anAddress("Admin", "i1_test@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@qq.com")));
    }

    @Test(expected = BusinessException.class)
    public void should_throw_business_exception_when_password_is_incorrect() {
        emailService = new EmailService(new EmailConfiguration(25, "smtp.163.com", true, "i1_test", "invalid password"), entityManager);

        emailService.sendEmail(anEmail(anAddress("Admin", "i1_test@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@qq.com")));
    }

    @Test(expected = BusinessException.class)
    public void should_throw_business_exception_when_address_does_not_exist() {
        emailService = new EmailService(new EmailConfiguration(25, "smtp.163.com", true, "i1_test", "ThoughtWorks"), entityManager);

        emailService.sendEmail(anEmail(anAddress("__$$__#$$#$fdfa@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@qq.com")));
    }

    @Test(expected = SystemException.class)
    public void should_throw_system_exception_when_there_are_system_errors() {
        emailService = new EmailService(new EmailConfiguration(25, "smtp.163.com", false, "i1_test", "ThoughtWorks"), entityManager);

        emailService.sendEmail(anEmail(anAddress("__$$__#$$#$fdfa@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@qq.com")));
    }

    @Ignore("Should setup qq mail to support smtp")
    public void should_send_email_with_qq_as_smtp_server() {
        emailConfiguration = new EmailConfiguration(25, "smtp.qq.com", true, "i1_test", "ThoughtWorks");
        emailService = new EmailService(emailConfiguration, entityManager);

        emailService.sendEmail(anEmail(anAddress("Admin", "i1_test@qq.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "i1_test@163.com")));
    }
}