package com.thoughtworks.i1.quartz.api;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.test.I1TestApplication;
import com.thoughtworks.i1.quartz.QuartzApplication;
import org.junit.Ignore;

import java.util.Collection;

public class QuartzTestApplication {
//public class QuartzTestApplication extends I1TestApplication {
//    @Ignore
//    @Override
//    protected Configuration defaultConfiguration() {
//        return Configuration.config()
//                .app().contextPath("/schedule").end()
//                .http().port(8052).end()
//                .database().persistUnit("domain").with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
//                .build();
//    }
//
//    @Ignore
//    @Override
//    protected Collection<? extends Module> getCustomizedModules() {
//        return ImmutableList.of(new UriModule(getUri()), new QuartzApplication.QuartzModule());
//    }
}