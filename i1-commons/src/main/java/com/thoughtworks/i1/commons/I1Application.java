package com.thoughtworks.i1.commons;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.server.Embedded;
import com.thoughtworks.i1.commons.web.I1AssetServlet;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.google.common.base.Joiner.on;
import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;

public abstract class I1Application {

    private Configuration configuration;
    private Embedded server;

    public I1Application() {
    }

    public Embedded start(boolean standalone) {
        return getServer().start(standalone);
    }

    private Embedded getServer() {
        if (server == null) {
            server = Embedded.jetty(getConfiguration().getHttp()).addServletContext(getConfiguration().getApp().getContextPath(), true, allModules());
        }
        return server;
    }

    public Configuration getConfiguration() {
        if (configuration == null) {
            configuration = defaultConfiguration();
        }
        return configuration;
    }

    public Injector getInjector() {
        return getServer().injector();
    }

    public void stop() {
        server.stop();
    }

    public URI getUri() {
        return getConfiguration().getHttp().getUri(getConfiguration().getApp().getContextPath());
    }

    public Module[] allModules() {
        List<Module> modules = Lists.newArrayList();
        modules.add(new I1JerseyServletModule(getConfiguration().getApp().getApiPrefix(), getConfiguration().getApp().getScanningPackage()));
        modules.add(new PropertyModule(getConfiguration().getApp().getPropertyFiles()));
        modules.add(new JpaPersistModule(getConfiguration().getDatabase().getPersistUnit()).properties(getConfiguration().getDatabase().toProperties()));
        modules.add(new I1AssertModule());
        modules.addAll(getCustomizedModules());

        return modules.toArray(new Module[0]);
    }

    protected Collection<? extends Module> getCustomizedModules() {
        return Collections.EMPTY_LIST;
    }

    protected abstract Configuration defaultConfiguration();

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

    public static class I1JerseyServletModule extends JerseyServletModule {
        private final String prefix;
        private final String[] packages;

        public I1JerseyServletModule(String prefix, String... packages) {
            this.prefix = prefix;
            this.packages = packages;
        }

        @Override
        protected void configureServlets() {
            bind(JacksonJaxbJsonProvider.class).in(Singleton.class);
            serve(prefix).with(GuiceContainer.class, new ImmutableMap.Builder<String, String>()
                    .put(PROPERTY_PACKAGES, on(";").skipNulls().join(packages)).build());
            // we only open entityManager when user is accessing api
            filter(prefix).through(PersistFilter.class);
        }
    }

    public static class I1AssertModule extends ServletModule {
        @Override
        protected void configureServlets() {
            String htmlInfo = "html,js,css,fonts,img,index.html";
            Iterable<String> iterable = Splitter.on(',').split(htmlInfo);
            for(String html : iterable){
                if(html.equals("index.html")){
                    serve("/" + html).with(new I1AssetServlet(html));
                } else {
                    serve("/" + html + "/*").with(new I1AssetServlet(html));
                }
            }
        }
    }
}
