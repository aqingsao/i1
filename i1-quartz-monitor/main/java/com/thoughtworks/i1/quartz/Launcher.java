package com.thoughtworks.i1.quartz;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.thoughtworks.i1.quartz.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Launcher {
    public static void main(String[] args) throws Exception {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        handler.addServlet(DefaultServlet.class, "/*");
        handler.setContextPath("/schedule");

        Server server = new Server(8089);
        server.setHandler(handler);

        server.start();

        final Injector injector = Guice.createInjector();

        handler.addEventListener(new MyGuiceServletContextListener());

    }
}
