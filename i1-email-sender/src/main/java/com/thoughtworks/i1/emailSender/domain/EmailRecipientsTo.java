package com.thoughtworks.i1.emailSender.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TO")
public class EmailRecipientsTo extends EmailRecipients {
    public EmailRecipientsTo(Address address){
        super(address);
    }

    public EmailRecipientsTo(){
    }
}
