package com.thoughtworks.i1.emailSender.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.thoughtworks.i1.emailSender.domain.Email;
import com.thoughtworks.i1.emailSender.service.EmailService;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyGuiceServletContextListener extends GuiceServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(MyGuiceServletContextListener.class.getName());

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(Email.class).to(Email.class);
                bind(EmailService.class).to(EmailService.class);
            }
        });
    }
}