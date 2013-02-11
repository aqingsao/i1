package com.thoughtworks.i1.emailSender.domain;

import javax.persistence.Entity;

public class SendingEmailError {
    private String message;

    public SendingEmailError() {
    }

    public SendingEmailError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
