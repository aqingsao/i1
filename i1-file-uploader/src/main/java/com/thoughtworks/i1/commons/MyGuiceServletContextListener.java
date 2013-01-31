package com.thoughtworks.i1.commons;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.thoughtworks.i1.upload.UploadServlet;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(MyGuiceServletContextListener.class.getName());

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            protected void configureServlets() {
                Names.bindProperties(binder(), loadProperties());
                serve("/upload").with(UploadServlet.class);
            }
        });
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        String propertyFile = "/app.properties";
        try {
            properties.load(getClass().getResourceAsStream(propertyFile));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load property file " + propertyFile);
        }
        return properties;
    }
}