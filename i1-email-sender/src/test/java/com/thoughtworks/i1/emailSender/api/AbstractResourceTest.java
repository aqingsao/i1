package com.thoughtworks.i1.emailSender.api;

import com.google.inject.servlet.GuiceFilter;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.config.H2;
import com.thoughtworks.i1.commons.config.Hibernate;
import com.thoughtworks.i1.commons.db.Migration;
import com.thoughtworks.i1.emailSender.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.persistence.*;
import javax.servlet.DispatcherType;
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
        DatabaseConfiguration configuration = DatabaseConfiguration.database().user("sa").password("")
                .with(H2.driver, H2.fileDB("/Users/twer/Projects/i1/email-sender"), H2.compatible("Oracle"), Hibernate.create, Hibernate.dialect("Oracle10g"), Hibernate.showSql)
                .build();
        Migration.migrate(configuration);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("domain", configuration.toProperties());
        entityManager.set(entityManagerFactory.createEntityManager());

        handler.addEventListener(new MyGuiceServletContextListener(configuration));
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

    protected <T> void removeAll(Class<T> entityClass) {
        EntityManager entityManager = getEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        TypedQuery<T> query = entityManager.createQuery("select o from " + entityClass.getName() + " o", entityClass);
        query.getResultList();
        for (T t : query.getResultList()) {
            entityManager.remove(t);
        }
        transaction.commit();
    }
}