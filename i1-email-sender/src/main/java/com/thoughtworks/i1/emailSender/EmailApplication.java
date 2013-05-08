package com.thoughtworks.i1.emailSender;

import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;

public class EmailApplication extends I1Application {

    public EmailApplication() {
    }

    @Override
    public Configuration defaultConfiguration() {
        return Configuration.config()
                .http().port(8052).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    @Override
    public String getContextPath() {
        return "/email";
    }

    @Override
    protected String[] getPropertyFiles() {
        return new String[]{"email-sender.properties"};
    }

    public static void main(String[] args) throws Exception {
        new EmailApplication().start(true);
    }
}
