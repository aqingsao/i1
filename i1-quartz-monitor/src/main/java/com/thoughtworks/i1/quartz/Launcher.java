package com.thoughtworks.i1.quartz;

import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.server.Embedded;
import com.thoughtworks.i1.quartz.service.QuartzModule;
import com.thoughtworks.i1.quartz.web.QuartzServletModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) throws Exception {
        Configuration configuration = Configuration.config().http().port(8051).end().build();
        Embedded.jetty(configuration.getHttp()).addServletContext("/schedule", true, new QuartzServletModule(), new QuartzModule()).start(true);

        LOGGER.info("Server started on port " + configuration.getHttp().getPort());
    }
}
