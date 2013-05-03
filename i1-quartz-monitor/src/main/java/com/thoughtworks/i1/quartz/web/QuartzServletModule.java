package com.thoughtworks.i1.quartz.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Scopes;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.commons.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;

public class QuartzServletModule extends ServletModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzServletModule.class);

    @Override
    protected void configureServlets() {
        bindProperties(binder(), loadProperties("quartz-monitor.properties"));
        bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);

        serve("/api/*").with(GuiceContainer.class, new ImmutableMap.Builder<String, String>()
                .put(PROPERTY_PACKAGES, "com.thoughtworks.i1").put(FEATURE_POJO_MAPPING, "true").build());
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
