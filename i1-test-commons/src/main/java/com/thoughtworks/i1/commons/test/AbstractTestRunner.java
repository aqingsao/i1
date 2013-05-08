package com.thoughtworks.i1.commons.test;

import com.google.common.base.Throwables;
import com.google.inject.Injector;
import com.googlecode.flyway.core.Flyway;
import com.thoughtworks.i1.commons.I1Application;
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
    private I1Application application;

    public AbstractTestRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        application = getApplication(klass);
    }

    private I1Application getApplication(Class<?> klass) {
        if (klass == null) {
            return null;
        }
        TestApplication annotation = klass.getAnnotation(TestApplication.class);
        if (annotation != null) {
            Class<? extends I1Application> value = annotation.value();
            try {
                return value.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(String.format("Cannot instantiate application for %s", value.getName()));
            }
        }
        if (klass == Object.class) {
            throw new RuntimeException(String.format("Cannot find expected annotation %s on test class",
                    TestApplication.class.getName()));
        }
        return this.getApplication(klass.getSuperclass());
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
        startServer();
    }

    protected void afterAllTestsRun() {
        closeServer();
        cleanDatabase(application.getConfiguration().getDatabase());
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

    protected void startServer() {
        try {
            migrateDatabase(application.getConfiguration().getDatabase());

            client = new HttpClient();
            client.start();

            final String contextPath = "/test";

            server = application.runInEmbeddedJetty(false);

            LOGGER.info(String.format("Server is started at: %s", application.getConfiguration().getHttp().getUri(contextPath)));
            injector = server.injector();
            entityManager = injector.getInstance(EntityManager.class);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void closeServer() {
        try {
            if (server != null) {
                server.stop();
                LOGGER.info("Server is stopped successfully");
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
