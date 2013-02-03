package com.thoughtworks.i1.emailSender.domain;

import com.google.common.base.Preconditions;

public class Sender {
    private Address from;
    private Address replyTo;

    private Sender() {
    }

    private Sender(Address from, Address replyTo) {
        Preconditions.checkNotNull(from, "Missing sender");
        this.from = from;
        this.replyTo = replyTo;
    }

    public static Sender aSender(Address from) {
        return new Sender(from, null);
    }

    public static Sender aSender(String userName, String userEmail) {
        return aSender(Address.anAddress(userName, userEmail));
    }

    public static Sender aSender(String userEmail) {
        return aSender(Address.anAddress(userEmail));
    }

    public static Sender aSender(Address from, Address replyTo) {
        return new Sender(from, replyTo);
    }

    public Address getFrom() {
        return from;
    }

    public Address getReplyTo() {
        return replyTo;
    }

    public void validate() {
        if (!this.from.isValid()) {
            throw new IllegalArgumentException("Invalid from address");
        }
        if (this.replyTo != null && !this.replyTo.isValid()) {
            throw new IllegalArgumentException("Invalid replyTo address");
        }
    }
}
