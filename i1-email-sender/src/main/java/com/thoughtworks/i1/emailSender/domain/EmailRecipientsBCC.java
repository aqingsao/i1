package com.thoughtworks.i1.emailSender.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("BCC")
public class EmailRecipientsBCC extends EmailRecipients {

//    @ManyToOne
//    @JoinColumn(name = "EMAIL_RECIPIENTS_EMAIL_ID")
//    protected Email email;

    public EmailRecipientsBCC(Address address) {
        super(address);
    }

    public EmailRecipientsBCC(){
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
