package com.thoughtworks.i1.emailSender;

import com.google.inject.servlet.GuiceFilter;
import com.thoughtworks.i1.emailSender.commons.DatabaseConfiguration;
import com.thoughtworks.i1.emailSender.commons.H2;
import com.thoughtworks.i1.emailSender.commons.Hibernate;
import com.thoughtworks.i1.emailSender.commons.Migration;
import com.thoughtworks.i1.emailSender.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.io.File;
import java.util.EnumSet;

import static com.google.inject.name.Names.bindProperties;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class Main {
    public static final String CONTEXT_PATH = "/email-sender";
    public static final int PORT = 8052;
    public static final String RESOURCE_BASE = new File(new File(Main.class.getClassLoader().getResource(".").getPath()).getParentFile(), "src/main/webapp").getAbsolutePath();

    protected static Server createServer() {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, CONTEXT_PATH, NO_SESSIONS);
        handler.setResourceBase(RESOURCE_BASE);

        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        // Must add DefaultServlet for embedded Jetty, failing to do this will cause 404 errors.
        // This is not needed if web.xml is used instead.
        handler.addServlet(DefaultServlet.class, "/*");
        DatabaseConfiguration configuration = DatabaseConfiguration.database().user("user").password("")
                .with(H2.driver, H2.tempFileDB, H2.compatible("Oracle"), Hibernate.createDrop, Hibernate.dialect("Oracle10g"), Hibernate.showSql).build();

        Migration.migrate(configuration);

        handler.addEventListener(new MyGuiceServletContextListener(configuration));

        return server;
    }

    public static void main(String[] args) throws Exception {
        createServer().start();
        System.in.read();
    }
}