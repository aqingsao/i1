package com.thoughtworks.i1.quartz.api;

import com.google.common.base.Optional;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.test.I1TestApplication;
import com.thoughtworks.i1.quartz.QuartzApplication;

public class QuartzTestApplication extends I1TestApplication {
    @Override
    protected Configuration defaultConfiguration() {
        return Configuration.config()
                .http().port(8052).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    @Override
    protected Optional<QuartzApplication.QuartzModule> getCustomizedModule() {
        return Optional.of(new QuartzApplication.QuartzModule());
    }
}
