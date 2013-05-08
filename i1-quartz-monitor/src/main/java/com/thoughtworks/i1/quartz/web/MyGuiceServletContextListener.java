package com.thoughtworks.i1.quartz.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.thoughtworks.i1.quartz.Application;
import com.thoughtworks.i1.quartz.service.QuartzModule;

// This listener is used for both web.xml and embedded server
public class MyGuiceServletContextListener extends GuiceServletContextListener {

    private final Application application;

    public MyGuiceServletContextListener() {
        application = new Application();
    }

    @Override
    public Injector getInjector() {
        return Guice.createInjector(application.jerseyServletModule("/api/*", "com.thoughtworks.i1"),
                application.jpaPersistModule("domain"), new QuartzModule());
    }
}