package com.thoughtworks.i1.quartz.web;

import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.thoughtworks.i1.commons.Modules;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.quartz.service.QuartzModule;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MyGuiceServletContextListener.class);

    public MyGuiceServletContextListener() {
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new Modules().jerseyServletModule("/api/*", Optional.<String>absent(), "com.thoughtworks.i1"),
                new QuartzModule());
    }

    private Properties loadProperties(String propertyFile) {

        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(propertyFile));
            return properties;
        } catch (IOException e) {
            LOGGER.error("Failed to load property file " + propertyFile);
            throw new SystemException(e.getMessage(), e);
        }
    }
}