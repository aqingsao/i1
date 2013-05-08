package com.thoughtworks.i1.commons;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.server.Embedded;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import static com.google.common.base.Joiner.on;
import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;

public abstract class I1Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(I1Application.class);
    private Configuration configuration;
    private Embedded server;

    public Embedded runInEmbeddedJetty(boolean standalone) {
        server = Embedded.jetty(getConfiguration().getHttp());
        return server.addServletContext(getContextPath(), true, getModules()).start(standalone);
    }

    protected String getPersistUnit() {
        return "domain";
    }

    protected String getScanningPackage() {
        return "com.thoughtworks.i1";
    }

    protected String getApiPrefix() {
        return "/api/*";
    }

    protected Module[] getModules() {
        Module propertyModule = getPropertyModule();
        Module jerseyServletModule = jerseyServletModule(getApiPrefix(), getScanningPackage());
        Module jpaPersistModule = jpaPersistModule(getPersistUnit());
        Optional<Module> customizedModule = getCustomizedModule();
        if (customizedModule.isPresent()) {
            return new Module[]{propertyModule, jerseyServletModule, jpaPersistModule, customizedModule.get()};
        }
        return new Module[]{propertyModule, jerseyServletModule, jpaPersistModule};
    }

    public JpaPersistModule jpaPersistModule(String persistUnit) {
        return new JpaPersistModule(persistUnit).properties(getConfiguration().getDatabase().toProperties());
    }

    public JerseyServletModule jerseyServletModule(final String prefix, final String... packages) {
        return new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(JacksonJaxbJsonProvider.class).in(Singleton.class);
                serve(prefix).with(GuiceContainer.class, new ImmutableMap.Builder<String, String>()
                        .put(PROPERTY_PACKAGES, on(";").skipNulls().join(packages)).build());
                // we only open entityManager when user is accessing api
                filter(prefix).through(PersistFilter.class);
            }
        };
    }

    public Configuration getConfiguration() {
        if (configuration == null) {
            configuration = defaultConfiguration();
        }
        return configuration;
    }

    public String getContextPath(){
        return "/";
    }

    public Injector getInjector(){
        return server.injector();
    }

    public void stop() {
        server.stop();
    }

    protected String[] getPropertyFiles() {
        return new String[0];
    }

    protected <T extends Module> Optional<T> getCustomizedModule() {
        return Optional.absent();
    }

    protected abstract Configuration defaultConfiguration();

    protected Module getPropertyModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                for (String propertyFile : getPropertyFiles()) {
                    bindProperties(binder(), loadProperties(propertyFile));
                }
            }
        };
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

    public URI getUri() {
        return this.configuration.getHttp().getUri(getContextPath());
    }
}
