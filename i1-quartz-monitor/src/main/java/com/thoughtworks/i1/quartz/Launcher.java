package com.thoughtworks.i1.quartz;

import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.server.Embedded;
import com.thoughtworks.i1.quartz.service.QuartzModule;

public class Launcher {
    public static void main(String[] args) throws Exception {
        Configuration configuration = Configuration.config()
                .http().port(8051).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
        Embedded.jetty(configuration.getHttp()).addServletContext("/scheduler", true, new QuartzModule()).start(true);
    }

}
