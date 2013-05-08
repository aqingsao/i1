package com.thoughtworks.i1.emailSender.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.emailSender.EmailApplication;
import com.thoughtworks.i1.emailSender.service.EmailConfiguration;
import com.thoughtworks.i1.emailSender.service.EmailService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    private final I1Application application;

    public MyGuiceServletContextListener() {
        this.application = new EmailApplication();
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(application.jerseyServletModule("/api/*", "com.thoughtworks.i1"),
                application.jpaPersistModule("domain"), new AbstractModule() {
            @Override
            protected void configure() {
                bind(EmailService.class);
                bind(EmailConfiguration.class);
            }
        });
    }
}