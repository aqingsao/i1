package com.thoughtworks.i1.quartz.api;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.i1.quartz.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.DispatcherType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.EnumSet;

public abstract class AbstractResourceTest {
    private static ThreadLocal<EntityManager> entityManager = new ThreadLocal<>();
    protected static Server server = createServer();
    protected static final URI BASE_URI = baseURI();
    public static final String CONTEXT_PATH = "/email-sender";
    public static final int PORT = 8052;

    private static URI baseURI() {
        return UriBuilder.fromUri("http://localhost/").port(PORT).path(CONTEXT_PATH).build();
    }

    public AbstractResourceTest() {
    }

    protected static Server createServer() {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, CONTEXT_PATH);
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        // Must add DefaultServlet for embedded Jetty, failing to do this will cause 404 errors.
        // This is not needed if web.xml is used instead.
        handler.addServlet(DefaultServlet.class, "/*");

        handler.addEventListener(new MyGuiceServletContextListener());
        return server;
    }

    protected URI uri(String path) {
        return UriBuilder.fromUri(BASE_URI).path(path).build();
    }

    protected static EntityManager getEntityManager() {
        return entityManager.get();
    }

    protected static <T> T persist(T object) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(object);
        transaction.commit();
        return object;
    }

    protected static <T> T remove(T object) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(object);
        transaction.commit();
        return object;
    }

    protected ClientResponse get(String path) {
        WebResource webResource = Client.create().resource(uri(path));

        return webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    }
}