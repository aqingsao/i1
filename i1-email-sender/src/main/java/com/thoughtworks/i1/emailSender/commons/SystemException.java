package com.thoughtworks.i1.emailSender.commons;

public class SystemException extends RuntimeException{
    public SystemException(String message, Throwable e) {
        super(message, e);
    }
    public SystemException(String message) {
        super(message);
    }
}
