package com.thoughtworks.i1.email.model;

import com.thoughtworks.i1.email.EmailService;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static com.thoughtworks.i1.email.model.Recipients.oneRecipients;
import static com.thoughtworks.i1.email.model.Sender.aSender;

public class Email {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    protected String subject = null;
    private String message;
    private String[] attachments = new String[0];
    private Recipients recipients;

    private static final String TYPE_HTML_UTF_8 = "text/html; charset=UTF-8";
    private Sender sender;

    private Email(Sender sender, String subject, String message, Recipients recipients, String... attachments) {
        this.sender = sender;
        this.subject = subject;
        this.recipients = recipients;
        this.message = message;
        this.attachments = attachments;
    }

    public static Email anEmail(Address from, String subject, String message, Address to, String... attachments) {
        return new Email(aSender(from), subject, message, oneRecipients(to), attachments);
    }

    public static Email anEmail(Sender sender, String subject, String message, Recipients recipients, String... attachments) {
        return new Email(sender, subject, message, recipients, attachments);
    }

    public MimeMessage buildMessage(Session session) throws MessagingException {
        MimeMessage message = initMessage(session);

        MimeMultipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(asContentPart(this.getMessage()));

        for (String attachment : attachments) {
            LOGGER.info("Add attachment " + attachment);
            multiPart.addBodyPart(asAttachmentPart(attachment));
        }
        message.setContent(multiPart);

        return message;
    }

    private MimeBodyPart asAttachmentPart(String attachment) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();

        FileDataSource dataSource = new FileDataSource(attachment);
        attachmentPart.setDataHandler(new DataHandler(dataSource));
        attachmentPart.setFileName(dataSource.getName());
        return attachmentPart;
    }

    private MimeBodyPart asContentPart(String emailContent) throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailContent, TYPE_HTML_UTF_8);
        return bodyPart;
    }

    private MimeMessage initMessage(Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(sender.getFrom().toInternetAddress());
        message.setSubject(subject);
        message.setSentDate(new java.util.Date());

        if (sender.getReplyTo() != null) {
            message.setReplyTo(new javax.mail.Address[]{sender.getReplyTo().toInternetAddress()});
        }
        message.setRecipients(Message.RecipientType.TO, recipients.getToAddresses());
        message.setRecipients(Message.RecipientType.CC, recipients.getCCAddresses());
        message.setRecipients(Message.RecipientType.BCC, recipients.getBCCAddresses());

        return message;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public Recipients getRecipients() {
        return this.recipients;
    }
}
