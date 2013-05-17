package com.thoughtworks.i1.commons;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Errors {
    private String message;
    private List<Error> errorList = new ArrayList();

    public Errors() {
    }

    private Errors(String message) {
        this.message = message;
    }

    public static Errors errors(String message) {
        return new Errors(message);
    }

    public void add(String beanClass, String field, String message) {
        errorList.add(anError(beanClass, field, message));
    }

    private Error anError(String beanClass, String field, String message) {
        return new Error(beanClass, field, message);
    }

    public String getMessage() {
        return message;
    }

    public List<Error> getErrorList() {
        return this.errorList;
    }

    public boolean isEmpty() {
        return this.errorList.isEmpty();
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Error {
        private String beanClass;
        private String field;
        private String message;

        public Error() {
        }

        public Error(String beanClass, String field, String message) {
            this.beanClass = beanClass;
            this.field = field;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getBeanClass() {
            return beanClass;
        }

        public String getField() {
            return field;
        }
    }
}
