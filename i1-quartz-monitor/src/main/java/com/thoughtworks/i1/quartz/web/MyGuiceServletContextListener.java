package com.thoughtworks.i1.quartz.web;

import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thoughtworks.i1.commons.web.I1GuiceServletContextListener;
import com.thoughtworks.i1.quartz.Application;
import com.thoughtworks.i1.quartz.service.QuartzModule;

// This listener is used for both web.xml and embedded server
public class MyGuiceServletContextListener extends I1GuiceServletContextListener {

    private final Application application;

    public MyGuiceServletContextListener() {
        application = Application.getInstance();
    }

    @Override
    public Injector getInjector() {
        return Guice.createInjector(application.jerseyServletModule("/api/*", Optional.<String>absent(), "com.thoughtworks.i1"),
                application.jpaPersistModule("domain"), new QuartzModule());
    }
}