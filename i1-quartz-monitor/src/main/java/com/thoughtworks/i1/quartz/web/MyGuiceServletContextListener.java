package com.thoughtworks.i1.quartz.web;

import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.commons.Modules;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.web.I1GuiceServletContextListener;
import com.thoughtworks.i1.quartz.Application;
import com.thoughtworks.i1.quartz.service.QuartzModule;

public class MyGuiceServletContextListener extends I1GuiceServletContextListener {

    private final Configuration configuration;

    public MyGuiceServletContextListener() {
        configuration = Application.getInstance().getConfiguration();
    }

    @Override
    public Injector getInjector() {
        return Guice.createInjector(new Modules().jerseyServletModule("/api/*", Optional.<String>absent(), "com.thoughtworks.i1"),
                new Modules().jpaPersistModule("domain", configuration.getDatabase()), new QuartzModule());
    }
}