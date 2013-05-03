package com.thoughtworks.i1.quartz.web;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.quartz.service.QuartzModule;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;

public class MyGuiceServletContextListener extends GuiceServletContextListener {

    public MyGuiceServletContextListener() {
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new QuartzServletModule(), new QuartzModule());
    }
}