package com.thoughtworks.i1.quartz.service;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(SchedulerFactory.class).to(StdSchedulerFactory.class).in(Scopes.SINGLETON);
        bind(JobsService.class).in(Scopes.SINGLETON);
    }
}