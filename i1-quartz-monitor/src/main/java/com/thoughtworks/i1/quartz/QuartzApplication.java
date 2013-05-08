package com.thoughtworks.i1.quartz;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.quartz.service.JobsService;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzApplication extends I1Application {

    public QuartzApplication() {
    }

    @Override
    public Configuration defaultConfiguration() {
        return Configuration.config()
                .http().port(8052).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    @Override
    protected Optional<QuartzModule> getCustomizedModule() {
        return Optional.of(new QuartzModule());
    }

    @Override
    public String getContextPath() {
        return "/schedule";
    }

    public static void main(String[] args) throws Exception {
        new QuartzApplication().start(true);
    }

    public static class QuartzModule extends AbstractModule
    {
        @Override
        protected void configure()
        {
            bind(SchedulerFactory.class).to(StdSchedulerFactory.class).in(Scopes.SINGLETON);
            bind(JobsService.class).in(Scopes.SINGLETON);
        }
    }
}
