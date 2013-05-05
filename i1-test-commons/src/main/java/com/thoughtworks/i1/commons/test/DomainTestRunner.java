package com.thoughtworks.i1.commons.test;

import com.thoughtworks.i1.commons.util.DBUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.LoggerFactory;

public class DomainTestRunner extends AbstractTestRunner {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DomainTestRunner.class);

    public DomainTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
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
