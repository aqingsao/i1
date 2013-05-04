package com.thoughtworks.i1.quartz.domain;

import java.util.List;
import java.util.Map;

public class JobDataVO {

    private String key;
    private String value;

    public JobDataVO() {
    }

    public JobDataVO(String key, String value) {

        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
