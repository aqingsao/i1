package com.thoughtworks.i1.emailSender.api;

import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.test.I1TestApplication;

public class EmailTestApplication extends I1TestApplication {
    @Override
    protected Configuration defaultConfiguration() {
        return Configuration.config()
                .app().contextPath("/email").propertyFiles("email-sender.properties").end()
                .http().port(8052).end()
                .database().persistUnit("domain").with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }
}
