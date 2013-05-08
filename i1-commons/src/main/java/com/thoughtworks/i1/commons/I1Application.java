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
    public static final String DEFAULT_PERSIST_UNIT = "domain";
    public static final String DEFAULT_SCANNING_PACKAGE = "com.thoughtworks.i1";
    public static final String DEFAULT_API_PREFIX = "/api/*";

    private Configuration configuration;
    private Embedded server;

    public I1Application() {
        server = Embedded.jetty(getConfiguration().getHttp()).addServletContext(getContextPath(), true, getModules());
    }

    public Embedded start(boolean standalone) {
        return server.start(standalone);
    }

    protected String getPersistUnit() {
        return DEFAULT_PERSIST_UNIT;
    }

    protected String getScanningPackage() {
        return DEFAULT_SCANNING_PACKAGE;
    }

    protected String getApiPrefix() {
        return DEFAULT_API_PREFIX;
    }

    public Module[] getModules() {
        Module propertyModule = new PropertyModule(this.getPropertyFiles());
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

    public String getContextPath() {
        return "/";
    }

    public Injector getInjector() {
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

    public URI getUri() {
        return this.configuration.getHttp().getUri(getContextPath());
    }

    public static class PropertyModule extends AbstractModule {
        private String[] propertyFiles;

        public PropertyModule(String... propertyFiles) {
            this.propertyFiles = propertyFiles;
        }

        @Override
        protected void configure() {
            for (String propertyFile : propertyFiles) {
                bindProperties(binder(), loadProperties(propertyFile));
            }
        }

        private Properties loadProperties(String propertyFile) {

            try {
                Properties properties = new Properties();
                properties.load(getClass().getClassLoader().getResourceAsStream(propertyFile));
                return properties;
            } catch (IOException e) {
                throw new SystemException(String.format("Failed to load property file %s: %s", propertyFile, e.getMessage()), e);
            }
        }
    }
}
