package com.thoughtworks.i1.commons.web;

import com.thoughtworks.i1.commons.config.HttpConfiguration;
import org.eclipse.jetty.server.Server;

public class EmbeddedJetty implements Embedded{
    private Server server;

    public EmbeddedJetty(HttpConfiguration configuration) {
//        server = new Server(threadPool(configuration));
//        server.setConnectors(configureConnectors(configuration));
    }

    @Override
    public void run() {
//        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
//        handler.addServlet(DefaultServlet.class, "/*");
//        handler.setContextPath(contextPath);
//
//        Server server = new Server(port);
//        server.setHandler(handler);
//
//        server.start();
//
//        final Injector injector = Guice.createInjector();
//
//        handler.addEventListener(new MyGuiceServletContextListener());
    }
}
