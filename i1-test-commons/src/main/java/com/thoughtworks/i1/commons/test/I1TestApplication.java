package com.thoughtworks.i1.commons.test;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;

import java.net.URI;

public class I1TestApplication extends I1Application{
    @Override
    protected Configuration defaultConfiguration() {
        return Configuration.config()
                .http().port(8052).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    @Override
    public Module[] getModules() {
        Module propertyModule = new PropertyModule();
        Module jerseyServletModule = jerseyServletModule(getApiPrefix(), getScanningPackage());
        Module jpaPersistModule = jpaPersistModule(getPersistUnit());
        Module uriModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(URI.class).toInstance(I1TestApplication.this.getUri());
            }
        };
        Optional<Module> customizedModule = getCustomizedModule();
        if (customizedModule.isPresent()) {
            return new Module[]{propertyModule, jerseyServletModule, jpaPersistModule, uriModule, customizedModule.get()};
        }
        return new Module[]{propertyModule, jerseyServletModule, jpaPersistModule, uriModule};
    }
}
