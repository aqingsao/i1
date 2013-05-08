package com.thoughtworks.i1.quartz.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.SystemException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

// This listener is used for both web.xml and embedded server
public class MyGuiceServletContextListener extends GuiceServletContextListener {

    private I1Application application;

    public MyGuiceServletContextListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        ServletContext c = servletContextEvent.getServletContext();
        String application = c.getInitParameter("application");
        if (application == null) {
            throw new SystemException("Cannot find application definition for context listener");
        }

        try {
            this.application = (I1Application) Class.forName(application).newInstance();
        } catch (Exception e) {
            throw new SystemException("Failed to instantiate application " + application);
        }

    }

    @Override
    public Injector getInjector() {
        return Guice.createInjector(application.getModules());
    }
}