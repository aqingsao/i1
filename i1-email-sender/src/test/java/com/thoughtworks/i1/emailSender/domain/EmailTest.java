package com.thoughtworks.i1.emailSender.domain;

import org.junit.Test;

public class EmailTest {
    @Test
    public void should_not_throw_exception_when_email_is_valid() {
        Email email = Email.anEmail(Sender.aSender(Address.anAddress("a@b.com")), "subject", "message", Recipients.oneRecipients(Address.anAddress("b@c.com")));
        email.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_sender_email_address_not_valid() {
        Email email = Email.anEmail(Sender.aSender(Address.anAddress("invalid email")), "subject", "message", Recipients.oneRecipients(Address.anAddress("b@c.com")));
        email.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_recipient_email_address_not_valid() {
        Email email = Email.anEmail(Sender.aSender(Address.anAddress("a@b.com")), "subject", "message", Recipients.oneRecipients(Address.anAddress("invalid email")));
        email.validate();
    }
}
