package com.thoughtworks.i1.emailSender.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Preconditions;

import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "I1_EMAIL_ADDRESS")
@Table(name = "i1_EMAIL_ADDRESS")
//@Table(name = "i1_EMAIL_ADDRESS", uniqueConstraints = @UniqueConstraint(columnNames = {"EMAIL_ADDRESS_NAME", "EMAIL_ADDRESS_ADDRESS"}))
public class Address {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    @Id
    @Column(name = "EMAIL_ADDRESS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "i1EmailAddressSeq")
    @SequenceGenerator(initialValue = 1, name = "i1EmailAddressSeq", sequenceName = "I1_EMAIL_ADDRESS_SEQUENCE")
    private long id;

    @Column(name = "EMAIL_ADDRESS_NAME")
    private String userName;
    @Column(name = "EMAIL_ADDRESS_ADDRESS", nullable = false)
    private String userAddress;

    private Address() {
    }

    private Address(String userName, String userAddress) {
        Preconditions.checkNotNull(userAddress);
        this.userName = userName;
        this.userAddress = userAddress;
    }

    public static Address anAddress(String userName, String userAddress) {
        return new Address(userName, userAddress);
    }

    public static Address anAddress(String userAddress) {
        return new Address(userAddress.split("@")[0], userAddress);
    }

    static InternetAddress[] toInternetAddresses(List<Address> addresses) {
        if (addresses == null) {
            return new InternetAddress[0];
        }

        InternetAddress[] internetAddresses = new InternetAddress[addresses.size()];
        int count = 0;
        for (Address address : addresses) {
            internetAddresses[count++] = address.toInternetAddress();
        }

        return internetAddresses;
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
        final Address other = (Address)o;

        return Objects.equals(this.id, other.id) && Objects.equals(this.userName, other.userName) && Objects.equals(this.userAddress, other.userAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.userName, this.userAddress);
    }

    @XmlTransient
    public boolean isValid() {
        return EMAIL_PATTERN.matcher(this.userAddress).matches();
    }
}
