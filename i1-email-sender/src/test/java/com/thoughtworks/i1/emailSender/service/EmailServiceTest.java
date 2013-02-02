package com.thoughtworks.i1.emailSender.service;

import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.thoughtworks.i1.emailSender.domain.Email;
import org.junit.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.thoughtworks.i1.emailSender.domain.Address.anAddress;
import static com.thoughtworks.i1.emailSender.domain.Email.anEmail;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmailServiceTest {

    private EmailService emailService;
    private EmailConfiguration emailConfiguration;
    private GreenMail mailServer;

    @Before
    public void before() {
        emailConfiguration = new EmailConfiguration();
        emailConfiguration.setMailServerPort(ServerSetupTest.SMTP.getPort());
        emailService = new EmailService(emailConfiguration);

        mailServer = new GreenMail(ServerSetupTest.SMTP);
        mailServer.start();
    }

    @After
    public void afterClass() {
        mailServer.stop();
    }

    @Test
    public void test_send_email() throws MessagingException, UserException {
        String body = "test body";
        String subject = "no subject";
        Email email = anEmail(anAddress("vinci_zhang@163.com"), subject, body, anAddress("vinci.zhang@gmail.com"));

        emailService.sendEmail(email);

        assertThat(mailServer.getReceivedMessages().length, is(1));
        MimeMessage msg = mailServer.getReceivedMessages()[0];
        String wholeMessage = GreenMailUtil.getWholeMessage(msg);
        assertThat(wholeMessage.contains(body), is(true));
        assertThat(wholeMessage.contains(subject), is(true));
    }

    @Test
    public void test_send_email_with_1_attachment() {
        String attachmentFile = "attachment1.txt";
        String attachment = this.getClass().getClassLoader().getResource(attachmentFile).getFile();
        Email email = anEmail(anAddress("Admin", "vinci_zhang@163.com"), "a test email", "test body", anAddress("Xiaoqing Zhang", "vinci.zhang@gmail.com"), attachment);
        emailService.sendEmail(email);

        assertThat(mailServer.getReceivedMessages().length, is(1));
        String wholeMessage = GreenMailUtil.getWholeMessage(mailServer.getReceivedMessages()[0]);
        assertThat(wholeMessage.contains(attachmentFile), is(true));
    }
}