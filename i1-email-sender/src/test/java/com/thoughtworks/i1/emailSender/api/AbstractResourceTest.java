package com.thoughtworks.i1.emailSender.api;

import com.google.inject.servlet.GuiceFilter;
import com.thoughtworks.i1.emailSender.commons.DatabaseConfiguration;
import com.thoughtworks.i1.emailSender.commons.H2;
import com.thoughtworks.i1.emailSender.commons.Hibernate;
import com.thoughtworks.i1.emailSender.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.EnumSet;

import static com.google.common.base.Throwables.propagate;

public abstract class AbstractResourceTest{
    protected static Server server = createServer();
    protected static final URI BASE_URI = baseURI();
    public static final String CONTEXT_PATH = "/email-sender";
    public static final int PORT = 8052;

    private static URI baseURI() {
        return UriBuilder.fromUri("http://localhost/").port(PORT).path(CONTEXT_PATH).build();
    }

    public AbstractResourceTest(){
    }

    protected static Server createServer() {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, CONTEXT_PATH);
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        // Must add DefaultServlet for embedded Jetty, failing to do this will cause 404 errors.
        // This is not needed if web.xml is used instead.
        handler.addServlet(DefaultServlet.class, "/*");
        DatabaseConfiguration configuration = DatabaseConfiguration.database().user("user").password("")
                .with(H2.driver, H2.fileDB("/Users/twer/Projects/i1/h2_i0.db"), H2.compatible("Oracle"), Hibernate.createDrop, Hibernate.dialect("Oracle10g"), Hibernate.showSql).build();

        handler.addEventListener(new MyGuiceServletContextListener(configuration));
        return server;
    }

    protected URI uri(String path) {
        return UriBuilder.fromUri(BASE_URI).path(path).build();
    }
}
