package com.thoughtworks.i1.commons.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.SystemException;

import javax.servlet.ServletContextEvent;

/**
 * Used when deployed to a web server, usage:
 * <listener>
 * <listener-class>com.thoughtworks.i1.commons.web.I1GuiceServletContextListener</listener-class>
 * </listener>
 * <context-param>
 * <param-name>application</param-name>
 * <param-value>you application definitioni class name</param-value>
 * </context-param>
 */

public class I1GuiceServletContextListener extends GuiceServletContextListener {
    private I1Application application;

    public I1GuiceServletContextListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(initApplication(servletContextEvent));
    }

    private ServletContextEvent initApplication(ServletContextEvent servletContextEvent) {
        String applicationName = servletContextEvent.getServletContext().getInitParameter("applicationName");
        if (applicationName == null) {
            throw new SystemException("Cannot find application name definition for context listener");
        }

        try {
            application = (I1Application) Class.forName(applicationName).newInstance();
        } catch (Exception e) {
            throw new SystemException("Failed to instantiate applicationName " + applicationName, e);
        }

        return servletContextEvent;
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(application.allModules());
    }
}