package com.thoughtworks.i1.emailSender.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.service.EmailConfiguration;
import com.thoughtworks.i1.emailSender.service.EmailService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyGuiceServletContextListener.class);

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                Names.bindProperties(binder(), loadProperties());
                bind(Email.class).to(Email.class);
                bind(EmailConfiguration.class).to(EmailConfiguration.class);
                bind(EmailService.class).to(EmailService.class);
            }
        });
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        String propertyFile = "email-sender.properties";
        try {
            properties.load(getClass().getResourceAsStream(propertyFile));
        } catch (IOException e) {
            LOGGER.error("Failed to load property file " + propertyFile);
            throw new RuntimeException(e.getMessage(), e);
        }
        return properties;
    }
}