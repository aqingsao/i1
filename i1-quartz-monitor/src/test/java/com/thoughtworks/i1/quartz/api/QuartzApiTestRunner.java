package com.thoughtworks.i1.quartz.api;

import com.google.inject.Module;
import com.thoughtworks.i1.commons.test.AbstractTestRunner;
import com.thoughtworks.i1.quartz.service.QuartzModule;
import org.junit.runners.model.InitializationError;

public class QuartzApiTestRunner extends AbstractTestRunner {
    public QuartzApiTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Module customizedModule() {
        return new QuartzModule();
    }
}
