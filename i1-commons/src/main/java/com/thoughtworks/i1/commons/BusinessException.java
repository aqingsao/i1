package com.thoughtworks.i1.commons;

public class BusinessException extends RuntimeException{
    public BusinessException(String message, Throwable e) {
        super(message, e);
    }
    public BusinessException(String message) {
        super(message);
    }

}
