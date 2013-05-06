package com.thoughtworks.i1.quartz;

import com.thoughtworks.i1.commons.AbstractApplication;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.server.Embedded;
import com.thoughtworks.i1.quartz.web.MyGuiceServletContextListener;

public class Application extends AbstractApplication {
    private static final Application instance = new Application();
    private Application(){}

    public static Application getInstance(){
        return instance;
    }

    @Override
    public Configuration defaultConfiguration() {
        return Configuration.config()
                .http().port(8051).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Application().getConfiguration();
        Embedded.jetty(configuration.getHttp()).addServletContext("/scheduler", true, new MyGuiceServletContextListener()).start(true);
    }
}
