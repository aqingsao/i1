package com.thoughtworks.i1.emailSender.domain;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity(name = "EMAIL_RECIPIENTS")
public class Recipients {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(mappedBy = "recipients")
    private Email email;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Address> toAddresses;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Address> ccAddresses;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Address> bccAddresses;

    private Recipients() {
    }

    private Recipients(List<Address> toAddresses, List<Address> ccAddresses, List<Address> bccAddresses) {
        this.toAddresses = toAddresses;
        this.ccAddresses = ccAddresses;
        this.bccAddresses = bccAddresses;
    }

    public static Recipients oneRecipients(Address recipient) {
        checkNotNull(recipient, "Missing recipient");
        return manyRecipients(Arrays.asList(recipient), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    public static Recipients manyRecipients(List<Address> toAddress, List<Address> ccList, List<Address> bccList) {
        return new Recipients(toAddress, ccList, bccList);
    }

    public void addToAddress(Address... addresses) {
        for (Address address : addresses) {
            toAddresses.add(address);
        }
    }

    public void addCcAddress(Address... addresses) {
        for (Address address : addresses) {
            ccAddresses.add(address);
        }
    }

    public void addBccAddress(Address... addresses) {
        for (Address address : addresses) {
            bccAddresses.add(address);
        }
    }

    public void setToAddresses(List<Address> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public void setCCAddresses(List<Address> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public void setBCCAddresses(List<Address> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    public List<Address> getToAddresses() {
        return this.toAddresses;
    }

    public List<Address> getCCAddresses() {
        return this.ccAddresses;
    }

    public List<Address> getBCCAddresses() {
        return this.bccAddresses;
    }

    public void validate() {
        for (Address toAddress : toAddresses) {
            if(!toAddress.isValid()){
                throw new IllegalArgumentException("Invalid to address");
            }
        }

        for (Address ccAddress : ccAddresses) {
            if(!ccAddress.isValid()){
                throw new IllegalArgumentException("Invalid cc address");
            }
        }
        for (Address bccAddress : bccAddresses) {
            if(!bccAddress.isValid()){
                throw new IllegalArgumentException("Invalid bcc address");
            }
        }
    }
}