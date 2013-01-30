package com.thoughtworks.ms.email.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

public class Address {
    private String userName;
    private String userAddress;

    private Address(String userName, String userAddress) {
        this.userName = userName;
        this.userAddress = userAddress;
    }

    public static Address anAddress(String userName, String userAddress) {
        return new Address(userName, userAddress);
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public InternetAddress toInternetAddress() {
        try {
            return new InternetAddress(this.userAddress, this.userName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
