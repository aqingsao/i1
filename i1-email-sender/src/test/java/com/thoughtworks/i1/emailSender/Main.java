package com.thoughtworks.i1.emailSender;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.thoughtworks.i1.emailSender.api.EmailResource;
import com.thoughtworks.i1.emailSender.commons.DatabaseConfiguration;
import com.thoughtworks.i1.emailSender.commons.H2;
import com.thoughtworks.i1.emailSender.commons.Hibernate;
import com.thoughtworks.i1.emailSender.service.EmailConfiguration;
import com.thoughtworks.i1.emailSender.service.EmailService;
import com.thoughtworks.i1.emailSender.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.EnumSet;

import static com.google.inject.name.Names.bindProperties;
import static com.sun.jersey.api.core.PackagesResourceConfig.PROPERTY_PACKAGES;
import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class Main {
    public static final String CONTEXT_PATH = "/email-sender";
    public static final int PORT = 8051;

    protected static Server createServer() {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, CONTEXT_PATH, NO_SESSIONS);
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        // Must add DefaultServlet for embedded Jetty, failing to do this will cause 404 errors.
        // This is not needed if web.xml is used instead.
        handler.addServlet(DefaultServlet.class, "/*");
        DatabaseConfiguration configuration = DatabaseConfiguration.database().user("user").password("")
                .with(H2.driver, H2.privateMemoryDB, H2.compatible("Oracle"), Hibernate.createDrop, Hibernate.dialect("Oracle10g"), Hibernate.showSql).build();
        handler.addEventListener(new MyGuiceServletContextListener(configuration));

        return server;
    }

    public static void main(String[] args) throws Exception {
        createServer().start();
        System.in.read();
    }

}
