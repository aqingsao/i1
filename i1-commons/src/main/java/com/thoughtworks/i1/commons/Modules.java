package com.thoughtworks.i1.commons;

import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import javax.inject.Singleton;

import static com.google.common.base.Joiner.on;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;

public class Modules {

    public static JpaPersistModule jpaPersistModule(String persistUnit, DatabaseConfiguration database) {
        return new JpaPersistModule(persistUnit).properties(database.toProperties());
    }

    public static JerseyServletModule jerseyServletModule(final String prefix, final String... packages) {
        return new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(JacksonJaxbJsonProvider.class).in(Singleton.class);
                serve(prefix).with(GuiceContainer.class, new ImmutableMap.Builder<String, String>()
                        .put(PROPERTY_PACKAGES, on(";").skipNulls().join(packages)).build());
                filter("/*").through(PersistFilter.class);
            }
        };
    }

}
