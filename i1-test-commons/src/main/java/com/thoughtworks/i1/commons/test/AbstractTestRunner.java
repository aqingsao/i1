package com.thoughtworks.i1.commons.test;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.googlecode.flyway.core.Flyway;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.server.Embedded;
import org.eclipse.jetty.client.HttpClient;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Properties;

public abstract class AbstractTestRunner extends BlockJUnit4ClassRunner {

    protected static Injector injector;
    protected static EntityManager entityManager;
    private static Embedded server;
    private static HttpClient client;
    private static boolean listenerAdded = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTestRunner.class);
    private Configuration configuration;

    public AbstractTestRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
    }

    protected Properties flywayConfiguration(DatabaseConfiguration configuration) {
        Properties properties = new Properties();
        properties.put("flyway.driver", configuration.getDriver());
        properties.put("flyway.url", configuration.getUrl());
        properties.put("flyway.user", configuration.getUser());
        properties.put("flyway.password", configuration.getPassword());
        return properties;
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (!listenerAdded) {
            LOGGER.info("----------before run all tests");
            beforeAllTestsRun();

            notifier.addListener(new RunListener() {
                public void testRunFinished(Result result) throws Exception {
                    LOGGER.info("++++++++++after run all tests");
                    afterAllTestsRun();
                }
            });
            listenerAdded = true;
        }
        try {
            LOGGER.info("----------before run");
            beforeRun();
            super.run(notifier);
        } finally {
            LOGGER.info("++++++++++after run");
            afterRun();
        }
    }

    protected void beforeAllTestsRun() {
        configuration = Configuration.config().http().port(8051).end().build();
        startServer(configuration);
    }

    protected void afterAllTestsRun() {
        closeServer();
        cleanDatabase(configuration.getDatabase());
    }

    protected void afterRun() {
    }

    protected void beforeRun() {
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        try {
            LOGGER.info("---------before run child");
            beforeRunChild(method);

            try {
                super.runChild(method, notifier);
            } finally {
                LOGGER.info("++++++++++after run child");
                afterRunChild();
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }

    }

    protected void afterRunChild() {
    }

    protected void beforeRunChild(FrameworkMethod method) {
    }

    protected void migrateDatabase(DatabaseConfiguration config) {
        Flyway flyway = new Flyway();
        if (config.getMigration().isPresent()) {
            DatabaseConfiguration.MigrationConfiguration configuration = config.getMigration().get();
            flyway.setLocations(configuration.getLocations());
            flyway.setPlaceholders(configuration.getPlaceholders());
            if (!configuration.isAuto()) {
                return;
            }
        }
        flyway.setValidateOnMigrate(true);
        flyway.configure(flywayConfiguration(config));
        flyway.clean();
        flyway.init();
        flyway.migrate();
    }

    protected void cleanDatabase(DatabaseConfiguration config) {
        Flyway flyway = new Flyway();
        flyway.configure(flywayConfiguration(config));
    }

    protected void startServer(Configuration configuration) {
        try {
            migrateDatabase(configuration.getDatabase());

            client = new HttpClient();
            client.start();

            server = Embedded.jetty(configuration.getHttp()).addServletContext("/test", true, new AbstractModule() {
                @Override
                protected void configure() {
                    bind(HttpClient.class).toInstance(client);
                    bind(JacksonJaxbJsonProvider.class);
                }
            }).start(false);

            entityManager = server.injector().getInstance(EntityManager.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void closeServer() {
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to stop server: " + e.getMessage(), e);
        }
        try {
            if (client != null) {
                client.stop();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to stop client: " + e.getMessage(), e);
        }
    }

    @Override
    protected Object createTest() throws Exception {
        Object currentTest = super.createTest();
        injector.injectMembers(currentTest);

        return currentTest;
    }
}
