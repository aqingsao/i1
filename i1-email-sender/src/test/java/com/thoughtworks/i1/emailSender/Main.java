package com.thoughtworks.i1.emailSender;

import com.google.inject.servlet.GuiceFilter;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.db.Migration;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.web.MyGuiceServletContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.DispatcherType;
import java.io.File;
import java.util.EnumSet;

import static com.thoughtworks.i1.emailSender.domain.Address.anAddress;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class Main {
    public static final String CONTEXT_PATH = "/email-sender";
    public static final int PORT = 8052;
    public static final String RESOURCE_BASE = new File(new File(Main.class.getClassLoader().getResource(".").getPath()).getParentFile(), "src/main/webapp").getAbsolutePath();
    private static EntityManager entityManager;

    protected static Server createServer() {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, CONTEXT_PATH, NO_SESSIONS);
        handler.setResourceBase(RESOURCE_BASE);

        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        // Must add DefaultServlet for embedded Jetty, failing to do this will cause 404 errors.
        // This is not needed if web.xml is used instead.
        handler.addServlet(DefaultServlet.class, "/*");
        DatabaseConfiguration configuration = Configuration.config().database().user("user").password("")
                .with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("Oracle"), DatabaseConfiguration.Hibernate.createDrop, DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).build();
        Migration.migrate(configuration);
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("domain", configuration.toProperties());
        entityManager = entityManagerFactory.createEntityManager();

        handler.addEventListener(new MyGuiceServletContextListener(configuration));

        return server;
    }

    private static void prepareData() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(Email.anEmail(anAddress("admin@i1.com"), "subject", "This is a sample message", anAddress("i1_test@qq.com")).setStatus("SENDING"));
        entityManager.persist(Email.anEmail(anAddress("admin@thoughtworks.com"), "subject", "Import message from ThoughtWorks admin", anAddress("i1_test@163.com")).setStatus("SUCCESS"));
        entityManager.persist(Email.anEmail(anAddress("i1_test@163.com"), "subject", "just a test", anAddress("i1_test@qq.com")).setStatus("ERROR"));

        transaction.commit();
    }

    public static void main(String[] args) throws Exception {
        createServer().start();

        prepareData();

        System.in.read();
    }
}