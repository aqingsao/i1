package com.thoughtworks.i1.emailSender.domain;

import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Recipients {
    private List<Address> toAddresses;
    private List<Address> ccAddresses;
    private List<Address> bccAddresses;

    private Recipients() {
    }

    private Recipients(List<Address> toAddresses, List<Address> ccAddresses, List<Address> bccAddresses) {
        this.toAddresses = toAddresses;
        this.ccAddresses = ccAddresses;
        this.bccAddresses = bccAddresses;
    }

    public static Recipients oneRecipients(Address recipient) {
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

    public void setCCAddresses(List<Address> bccAddresses) {
        this.bccAddresses = bccAddresses;
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
}
