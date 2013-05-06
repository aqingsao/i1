package com.thoughtworks.i1.commons.web;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public abstract class I1GuiceServletContextListener extends GuiceServletContextListener{
    @Override
    public abstract Injector getInjector();
}
