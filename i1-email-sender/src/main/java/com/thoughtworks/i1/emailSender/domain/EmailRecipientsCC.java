package com.thoughtworks.i1.emailSender.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("CC")
public class EmailRecipientsCC extends EmailRecipients {

//    @ManyToOne
//    @JoinColumn(name = "EMAIL_RECIPIENTS_EMAIL_ID")
//    protected Email email;

    public EmailRecipientsCC(Address address) {
        super(address);
    }

    public EmailRecipientsCC(){
    }

    public void setEmail(Email email) {
        this.email = email;
    }

}
