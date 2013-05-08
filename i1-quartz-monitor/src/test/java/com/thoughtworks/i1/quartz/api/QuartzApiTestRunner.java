package com.thoughtworks.i1.quartz.api;

import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.test.AbstractTestRunner;
import com.thoughtworks.i1.quartz.Application;
import org.junit.runners.model.InitializationError;

public class QuartzApiTestRunner extends AbstractTestRunner {
    public QuartzApiTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected I1Application getApplication() {
        return Application.getInstance();
    }
}
