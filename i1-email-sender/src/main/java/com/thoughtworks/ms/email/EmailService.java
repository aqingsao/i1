package com.thoughtworks.ms.email;

import com.thoughtworks.ms.email.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService implements TransportListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";

    private static final String SMTP = "smtp";
    private EmailConfiguration props;

    public EmailService(EmailConfiguration props) {
        this.props = props;
    }

    public boolean sendEmail(Email mail) {
        LOGGER.info(String.format("Send email %s to %d recipients.", mail.getSubject(), mail.getRecipients().getToList().size()));

        Properties properties = initProperties(props);
        Session session = openSession(properties, props);

        try {
            LOGGER.debug(String.format("Start to send email with subject %s", mail.getSubject()));
            MimeMessage message = mail.buildMessage(session);
            sendEmail(session, message);
            LOGGER.debug(String.format("Finished to send email."));
            return true;
        } catch (MessagingException e) {
            LOGGER.warn("Failed to send eamil: " + e.getMessage(), e);
            return false;
        }
    }

    private Session openSession(Properties properties, final EmailConfiguration props) {
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getAuthenticationUserName(),
                        props.getAuthenticationPassword());
            }
        });
        session.setDebug(true);
        return session;
    }

    private Properties initProperties(EmailConfiguration props) {
        Properties properties = new Properties();
        properties.put(MAIL_SMTP_HOST, props.getMailServerHost());
        properties.put(MAIL_SMTP_PORT, props.getMailServerPort());

        if (props.isAuthenticationRequired()) {
            properties.put(MAIL_SMTP_AUTH, "true");
        }
        return properties;
    }

    private void sendEmail(Session session, MimeMessage message) throws MessagingException {
        message.saveChanges();

        Transport transport = session.getTransport(SMTP);
        transport.addTransportListener(this);
//        transport.connect(props.getMailServerHost(), props.getMailServerPort(), props.getAuthenticationUserName(), props.getAuthenticationPassword());

        transport.send(message);
        transport.close();
    }

    @Override
    public void messageDelivered(TransportEvent e) {
        LOGGER.info("Message was delivered successfully.");
    }

    @Override
    public void messageNotDelivered(TransportEvent e) {
        LOGGER.warn("Message was not delivered: " + e.getType());
    }

    @Override
    public void messagePartiallyDelivered(TransportEvent e) {
        LOGGER.warn("Message was partially delivered: " + e.getType());
    }
}
