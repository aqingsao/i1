package com.thoughtworks.i1.quartz;

import com.google.common.base.Optional;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.quartz.service.QuartzModule;

public class Application extends I1Application {
    private static final Application instance = new Application();

    private Application() {
    }

    public static Application getInstance() {
        return instance;
    }

    @Override
    public Configuration defaultConfiguration() {
        return Configuration.config()
                .http().port(8051).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    @Override
    protected Optional<QuartzModule> getCustomizedModule() {
        return Optional.of(new QuartzModule());
    }

    @Override
    protected String getContextPath() {
        return "/schedule";
    }

    public static void main(String[] args) throws Exception {
        Application.getInstance().runInEmbeddedJetty(true);
    }
}
