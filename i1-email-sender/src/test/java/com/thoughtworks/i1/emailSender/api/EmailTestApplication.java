package com.thoughtworks.i1.emailSender.api;

import com.thoughtworks.i1.commons.test.I1TestApplication;

public class EmailTestApplication extends I1TestApplication {
    @Override
    protected String[] getPropertyFiles() {
        return new String[]{"email-sender.properties"};
    }
}
