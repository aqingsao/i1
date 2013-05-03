package com.thoughtworks.i1.quartz;

import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.HttpConfiguration;
import com.thoughtworks.i1.commons.web.Embedded;
import com.thoughtworks.i1.commons.web.EmbeddedJetty;
import com.thoughtworks.i1.quartz.service.QuartzModule;

public class Launcher {
    public static void main(String[] args) throws Exception {
        Configuration configuration = Configuration.config().http().port(8051).end().build();
        Embedded.jetty(configuration.getHttp()).addServletContext("/scheduler", true, new QuartzModule()).start(true);
    }

}
