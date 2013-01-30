package com.thoughtworks.ms.email.model;

import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Recipients {
    private List<Address> toList;
    private List<Address> ccList;
    private List<Address> bccList;

    private Recipients(List<Address> toList, List<Address> ccList, List<Address> bccList) {
        this.toList = toList;
        this.ccList = ccList;
        this.bccList = bccList;
    }

    public static Recipients oneRecipients(Address recipient) {
        return manyRecipients(Arrays.asList(recipient), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    public static Recipients manyRecipients(List<Address> toAddress, List<Address> ccList, List<Address> bccList) {
        return new Recipients(toAddress, ccList, bccList);
    }

    public void addToAddress(Address... addresses) {
        for (Address address : addresses) {
            toList.add(address);
        }
    }

    public void addCcAddress(Address... addresses) {
        for (Address address : addresses) {
            ccList.add(address);
        }
    }

    public void addBccAddress(Address... addresses) {
        for (Address address : addresses) {
            bccList.add(address);
        }
    }

    public Address getToAddress() {
        return this.toList.get(0);
    }

    public InternetAddress[] getToAddresses() {
        return getAddresses(this.toList);
    }

    public InternetAddress[] getCCAddresses() {
        return getAddresses(this.ccList);
    }

    public InternetAddress[] getBCCAddresses() {
        return getAddresses(this.bccList);
    }

    private InternetAddress[] getAddresses(List<Address> addresses) {
        InternetAddress[] internetAddresses = new InternetAddress[addresses.size()];
        int count = 0;
        for (Address address : addresses) {
            internetAddresses[count++] = address.toInternetAddress();
        }

        return internetAddresses;
    }

    public List<Address> getToList() {
        return toList;
    }
}
