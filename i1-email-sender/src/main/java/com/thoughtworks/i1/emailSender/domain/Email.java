package com.thoughtworks.i1.emailSender.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.thoughtworks.i1.emailSender.service.EmailService;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.*;

import static com.thoughtworks.i1.emailSender.domain.Address.toInternetAddresses;

@Entity
@Table(name = "I1_EMAIL")
public class Email {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final String TYPE_HTML_UTF_8 = "text/html; charset=UTF-8";
    @Id
    @Column(name = "EMAIL_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "i1EmailSeq")
    @SequenceGenerator(initialValue = 1, name = "i1EmailSeq", sequenceName = "I1_EMAIL_SEQUENCE")
    private long id;

    @Column(name = "SUBJECT")
    protected String subject;
    @Column(name = "MESSAGE")
    private String message;
    @Embedded
    private Sender sender;
    @Embedded
    private Recipients recipients;

    private String[] attachments = new String[0];

    private Email(Sender sender, String subject, String message, Recipients recipients, String... attachments) {
        Preconditions.checkNotNull(sender, "Missing sender");
        Preconditions.checkNotNull(recipients, "Missing recipient");
        Preconditions.checkNotNull(subject, "Missing subject");
        Preconditions.checkNotNull(message, "Missing message");

        this.sender = sender;
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;

        for (EmailRecipientsTo recipient : this.recipients.getEmailRecipientsTo()) {
            recipient.setEmail(this);
        }
        for (EmailRecipientsCC recipient : this.recipients.getEmailRecipientsCC()) {
            recipient.setEmail(this);
        }
        for (EmailRecipientsBCC recipient : this.recipients.getEmailRecipientsBCC()) {
            recipient.setEmail(this);
        }
        this.attachments = attachments;
    }

    private Email() {
    }

    public static Email anEmail(Address from, String subject, String message, Address to, String... attachments) {
        return new Email(Sender.aSender(from), subject, message, Recipients.oneRecipients(to), attachments);
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
        message.setRecipients(Message.RecipientType.TO, toInternetAddresses(this.recipients.getToAddresses()));
        message.setRecipients(Message.RecipientType.CC, toInternetAddresses(this.recipients.getCCAddresses()));
        message.setRecipients(Message.RecipientType.BCC, toInternetAddresses(recipients.getBCCAddresses()));

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

    public Sender getSender() {
        return sender;
    }

    public void validate() {
        this.sender.validate();
        this.recipients.validate();
        if (Strings.isNullOrEmpty(this.subject)) {
            throw new IllegalArgumentException("Missing mail subject");
        }
        if (Strings.isNullOrEmpty(this.message)) {
            throw new IllegalArgumentException("Missing mail body");
        }
    }
}
