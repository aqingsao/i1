package com.thoughtworks.i1.commons.test;

import com.thoughtworks.i1.commons.util.DBUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.LoggerFactory;

public class TransactionalDomainTestRunner extends AbstractTestRunner {

    public TransactionalDomainTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void beforeAllTestsRun() {
        startServer();
    }

    @Override
    protected void afterAllTestsRun() {
        closeServer();
    }

    protected void beforeRunChild(FrameworkMethod method) {
        super.beforeRunChild(method);
        entityManager.getTransaction().begin();
    }

    protected void afterRunChild() {
        super.afterRunChild();
        DBUtils.rollBackQuietly(entityManager.getTransaction());
    }
}
