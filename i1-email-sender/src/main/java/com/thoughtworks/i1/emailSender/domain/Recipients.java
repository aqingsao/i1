package com.thoughtworks.i1.emailSender.domain;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Embeddable
public class Recipients {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="EMAIL_RECIPIENTS_EMAIL_ID", insertable=false, updatable=false)
    @Where(clause="EMAIL_RECIPIENTS_TYPE='TO'")
    //http://chriswongdevblog.blogspot.com/2009/10/polymorphic-one-to-many-relationships.html
    private List<EmailRecipientsTo> emailRecipientsTo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="EMAIL_RECIPIENTS_EMAIL_ID", insertable=false, updatable=false)
    @Where(clause="EMAIL_RECIPIENTS_TYPE='CC'")
    private List<EmailRecipientsCC> emailRecipientsCC;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="EMAIL_RECIPIENTS_EMAIL_ID", insertable=false, updatable=false)
    @Where(clause="EMAIL_RECIPIENTS_TYPE='BCC'")
    private List<EmailRecipientsBCC> emailRecipientsBCC;

    private Recipients() {
    }

    private Recipients(List<Address> toAddresses, List<Address> ccAddresses, List<Address> bccAddresses) {
        this.emailRecipientsTo = new ArrayList<>();
        for (Address address : toAddresses) {
            this.emailRecipientsTo.add(new EmailRecipientsTo(address));
        }
        this.emailRecipientsCC = new ArrayList<>();
        for (Address address : ccAddresses) {
            this.emailRecipientsCC.add(new EmailRecipientsCC(address));
        }
        this.emailRecipientsBCC = new ArrayList<>();
        for (Address address : bccAddresses) {
            this.emailRecipientsBCC.add(new EmailRecipientsBCC(address));
        }
    }

    public static Recipients oneRecipients(Address recipient) {
        checkNotNull(recipient, "Missing recipient");
        return manyRecipients(Arrays.asList(recipient), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    public static Recipients manyRecipients(List<Address> toAddress, List<Address> ccList, List<Address> bccList) {
        return new Recipients(toAddress, ccList, bccList);
    }

    public List<EmailRecipientsTo> getEmailRecipientsTo() {
        return emailRecipientsTo;
    }

    public List<EmailRecipientsCC> getEmailRecipientsCC() {
        return emailRecipientsCC;
    }

    public List<EmailRecipientsBCC> getEmailRecipientsBCC() {
        return emailRecipientsBCC;
    }

    public List<Address> getToAddresses() {
        List<Address> ret = new ArrayList<>();
        for (EmailRecipients recipient : emailRecipientsTo) {
            ret.add(recipient.getAddress());
        }
        return ret;
    }

    public List<Address> getCCAddresses() {
        List<Address> ret = new ArrayList<>();
        for (EmailRecipients recipient : emailRecipientsCC) {
            ret.add(recipient.getAddress());
        }
        return ret;
    }

    public List<Address> getBCCAddresses() {
        List<Address> ret = new ArrayList<>();
        for (EmailRecipients recipient : emailRecipientsBCC) {
            ret.add(recipient.getAddress());
        }
        return ret;
    }

    public void validate() {
        for (EmailRecipients address : emailRecipientsTo) {
            if (!address.getAddress().isValid()) {
                throw new IllegalArgumentException("Invalid to address");
            }
        }

        for (EmailRecipients address : emailRecipientsCC) {
            if (!address.getAddress().isValid()) {
                throw new IllegalArgumentException("Invalid cc address");
            }
        }
        for (EmailRecipientsBCC bccAddress : emailRecipientsBCC) {
            if (!bccAddress.getAddress().isValid()) {
                throw new IllegalArgumentException("Invalid bcc address");
            }
        }
    }
}