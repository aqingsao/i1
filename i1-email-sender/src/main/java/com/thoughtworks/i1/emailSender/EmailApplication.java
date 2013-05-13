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
                .app().contextPath("/email").propertyFiles("email-sender.properties").end()
                .http().port(8052).end()
                .database().persistUnit("domain").with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    public static void main(String[] args) throws Exception {
        new EmailApplication().start(true);
    }
}
