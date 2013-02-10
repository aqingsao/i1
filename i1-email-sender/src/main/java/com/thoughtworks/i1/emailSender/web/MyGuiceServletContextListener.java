package com.thoughtworks.i1.emailSender.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.emailSender.api.EmailResource;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.service.EmailConfiguration;
import com.thoughtworks.i1.emailSender.service.EmailService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyGuiceServletContextListener.class);

    public MyGuiceServletContextListener() {
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bindProperties(binder(), loadProperties());
                bind(EmailService.class);
                bind(EmailConfiguration.class);
                bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
                serve("/api/*").with(GuiceContainer.class, new ImmutableMap.Builder<String, String>()
                        .put(PROPERTY_PACKAGES, "com.thoughtworks.i1.emailSender.api").put(FEATURE_POJO_MAPPING, "true").build());
            }
        });
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        String propertyFile = "email-sender.properties";
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(propertyFile));
        } catch (IOException e) {
            LOGGER.error("Failed to load property file " + propertyFile);
            throw new RuntimeException(e.getMessage(), e);
        }
        return properties;
    }
}