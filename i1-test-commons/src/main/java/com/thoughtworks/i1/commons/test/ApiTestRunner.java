package com.thoughtworks.i1.commons.test;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiTestRunner extends AbstractTestRunner {

    public ApiTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected void beforeAllTestsRun() {
        startServer();
    }

    protected void afterAllTestsRun() {
        closeServer();
    }

    protected void beforeRunChild(FrameworkMethod method) {
        super.beforeRunChild(method);
    }

    protected void afterRunChild() {
        super.afterRunChild();
        if (entityManager != null) {
            try {
                entityManager.clear();
            } catch (Exception e) {
            }
        }
    }

}
