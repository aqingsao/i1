package com.thoughtworks.i1.emailSender.service;

import com.google.common.base.Preconditions;
import com.google.inject.persist.Transactional;
import com.thoughtworks.i1.commons.BusinessException;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.domain.EmailRecipients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";

    private static final String SMTP = "smtp";

    private EmailConfiguration configuration;
    private EntityManager entityManager;

    @Inject
    public EmailService(EmailConfiguration configuration, EntityManager entityManager) {
        this.configuration = configuration;
        this.entityManager = entityManager;
    }

    @Transactional(ignore = {RuntimeException.class})
    public void sendEmail(Email mail) {
        LOGGER.info(String.format("Send email %s to %d recipients.", mail.getSubject(), mail.getRecipients().getToAddresses().size()));

        try {
            mail.setStatus("SENDING");
            for (EmailRecipients emailRecipients : mail.getRecipients().getEmailRecipientsTo()) {
                emailRecipients.setEmail(mail);
            }
            for (EmailRecipients emailRecipients : mail.getRecipients().getEmailRecipientsCC()) {
                entityManager.persist(emailRecipients);
            }
            for (EmailRecipients emailRecipients : mail.getRecipients().getEmailRecipientsBCC()) {
                entityManager.persist(emailRecipients);
            }
            entityManager.persist(mail);

            LOGGER.info("persist mail successfully");
        } catch (Exception e) {
            throw new SystemException("Cannot persist mail: " + e.getMessage(), e);
        }

        Properties properties = initProperties(configuration);
        Session session = openSession(properties, configuration.getAuthenticationUserName(), configuration.getAuthenticationPassword());

        try {
            LOGGER.debug(String.format("Start to send email with subject %s", mail.getSubject()));
            MimeMessage message = mail.buildMessage(session);

            Transport transport = session.getTransport(SMTP);
            try {
                transport.connect(configuration.getMailServerHost(), configuration.getMailServerPort(), configuration.getAuthenticationUserName(), configuration.getAuthenticationPassword());
                transport.sendMessage(message, message.getAllRecipients());
            } finally {
                transport.close();
            }
            mail.setStatus("SUCCESS");
            mail.setUpdatedAt(new Date());
            entityManager.persist(mail);
            LOGGER.debug(String.format("Finished to send email."));
        } catch (AuthenticationFailedException | AddressException e) {
            mail.setStatus("ERROR");
            mail.setUpdatedAt(new Date());
            entityManager.persist(mail);
            throw new BusinessException(e.getMessage(), e);
        } catch (MessagingException e) {
            mail.setStatus("ERROR");
            mail.setUpdatedAt(new Date());
            entityManager.persist(mail);
            throw new SystemException(e.getMessage(), e);
        }
    }

    private Session openSession(Properties properties, final String authenticationUserName, final String authenticationPassword) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        authenticationUserName,
                        authenticationPassword);
            }
        });
        session.setDebug(true);
        return session;
    }

    private Properties initProperties(EmailConfiguration props) {
        Preconditions.checkNotNull(props.getMailServerHost(), "Mail server host cannot be null");
        Preconditions.checkNotNull(props.getMailServerPort() > 0, "Mail server port cannot be 0");

        Properties properties = new Properties();
        properties.put(MAIL_SMTP_HOST, props.getMailServerHost());
        properties.put(MAIL_SMTP_PORT, props.getMailServerPort());

        if (props.isAuthenticationRequired()) {
            properties.put(MAIL_SMTP_AUTH, "true");
        }
        return properties;
    }

    public void setConfiguration(EmailConfiguration configuration) {
        this.configuration = configuration;
    }

    public List<Email> findEmails() {
        return this.entityManager.createQuery("select e from Email e").getResultList();
    }
}
