package com.thoughtworks.i1.emailSender.email.model;

public class Sender {
    private Address from;
    private Address replyTo;

    private Sender(Address from, Address replyTo) {
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
        return aSender(Address.anAddress("", userEmail));
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
}
