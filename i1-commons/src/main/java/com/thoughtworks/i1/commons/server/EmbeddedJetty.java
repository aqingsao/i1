package com.thoughtworks.i1.commons.server;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.thoughtworks.i1.commons.SystemException;
import com.thoughtworks.i1.commons.config.HttpConfiguration;
import com.thoughtworks.i1.commons.util.Duration;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.annotation.Nullable;
import javax.servlet.DispatcherType;
import java.io.File;
import java.util.EnumSet;

import static com.google.common.collect.Iterables.toArray;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;
import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

public class EmbeddedJetty extends Embedded {
    private Server server;
    public static final String RESOURCE_BASE = new File(EmbeddedJetty.class.getClassLoader().getResource(".").getPath()).getAbsolutePath();

    protected EmbeddedJetty(HttpConfiguration configuration) {
        server = new Server(threadPool(configuration));
        server.setConnectors(configureConnectors(configuration));
    }

    @Override
    public Embedded addServletContext(String contextPath, boolean shareNothing, final Module... modules) {

        ServletContextHandler handler = new ServletContextHandler(shareNothing ? NO_SESSIONS : SESSIONS);
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        handler.addServlet(DefaultServlet.class, "/*");
        handler.setContextPath(contextPath);
        handler.setInitParameter("org.eclipse.jetty.servlet.Default.resourceBase", RESOURCE_BASE);

        handler.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return Guice.createInjector(modules);
            }
        });

        HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{getResourceHandler(), handler});

        server.setHandler(handlerList);

        return this;
    }

    private ResourceHandler getResourceHandler() {
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{"index.html"});

        resource_handler.setResourceBase(".");

        return resource_handler;
    }

    @Override
    public Embedded start(boolean standalone) {
        Preconditions.checkState(!server.isRunning(), "Server is already running.");
        try {
            server.start();
            if (standalone) server.join();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
        return this;
    }

    @Override
    public Embedded stop() {
        if (!server.isRunning()) {
            return this;
        }

        try {
            server.stop();
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
        return this;
    }

    private QueuedThreadPool threadPool(HttpConfiguration configuration) {
        return new QueuedThreadPool(configuration.getMaxThread(), configuration.getMinThread(), (int) configuration.getMaxIdleTime().value());
    }

    private String root(String name) {
        return name.startsWith("/") ? name : "/" + name;
    }

    private ServerConnector[] configureConnectors(HttpConfiguration configuration) {
        return new ServerConnector[]{
                configureHttp(configuration, configureConnector(configuration))
        };
    }

    private ServerConnector configureConnector(HttpConfiguration configuration) {
        return new ServerConnector(server, null, null, null, configuration.getAcceptorThreads(), configuration.getSelectorThreads(),
                connectionFactories(configuration));
    }

    private ConnectionFactory[] connectionFactories(HttpConfiguration configuration) {
        return toArray(new ImmutableList.Builder<ConnectionFactory>()
                .addAll(configuration.getSsl().transform(toSslConnectionFactory).asSet())
                .add(new HttpConnectionFactory()).build(), ConnectionFactory.class);
    }

    private ServerConnector configureHttp(HttpConfiguration configuration, ServerConnector connector) {
        for (String host : configuration.getHost().asSet()) connector.setHost(host);
        connector.setPort(configuration.getPort());
        connector.setAcceptQueueSize(configuration.getAcceptQueueSize());
        connector.setSoLingerTime(configuration.getSoLingerTime().transform(new Function<Duration, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable Duration input) {
                return (int) input.value();
            }
        }).or(-1));
        connector.setIdleTimeout(configuration.getIdleTimeout().value());
        return connector;
    }

    private static final Function<HttpConfiguration.SslConfiguration, ConnectionFactory> toSslConnectionFactory = new Function<HttpConfiguration.SslConfiguration, ConnectionFactory>() {
        @Nullable
        @Override
        public ConnectionFactory apply(@Nullable HttpConfiguration.SslConfiguration input) {
            return new SslConnectionFactory(configureTrustStore(input, configureKeyStore(input, new SslContextFactory())), HttpVersion.HTTP_1_1.asString());
        }

        private SslContextFactory configureTrustStore(HttpConfiguration.SslConfiguration input, SslContextFactory sslContextFactory) {
            for (String trustStorePath : input.getTrustStorePath().asSet())
                sslContextFactory.setTrustStorePath(trustStorePath);
            for (String trustStorePassword : input.getTrustStorePassword().asSet())
                sslContextFactory.setTrustStorePassword(trustStorePassword);
            sslContextFactory.setTrustStoreType(input.getTrustStoreType());
            return sslContextFactory;
        }

        private SslContextFactory configureKeyStore(HttpConfiguration.SslConfiguration input, SslContextFactory sslContextFactory) {
            for (String keyStorePath : input.getKeyStorePath().asSet())
                sslContextFactory.setKeyStorePath(keyStorePath);
            for (String keyStorePassword : input.getKeyStorePassword().asSet())
                sslContextFactory.setKeyStorePassword(keyStorePassword);
            for (String keyManagerPassword : input.getKeyManagerPassword().asSet())
                sslContextFactory.setKeyManagerPassword(keyManagerPassword);
            sslContextFactory.setKeyStoreType(input.getKeyStoreType());
            return sslContextFactory;
        }
    };

}
