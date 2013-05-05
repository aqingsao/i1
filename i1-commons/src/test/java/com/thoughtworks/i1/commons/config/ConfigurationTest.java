package com.thoughtworks.i1.commons.config;

import org.junit.Test;

import static com.google.common.base.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {
    @Test
    public void should_create_database_configuration(){
        Configuration configuration = Configuration.config().database().driver("driver").password("password").user("user").url("url").end().build();

        assertThat(configuration.getDatabase().getDriver(), is("driver"));
        assertThat(configuration.getDatabase().getUser(), is("user"));
        assertThat(configuration.getDatabase().getPassword(), is("password"));
        assertThat(configuration.getDatabase().getUrl(), is("url"));
    }
    @Test
    public void should_create_http_configuration(){
        Configuration configuration = Configuration.config().http().port(8051).host("host").acceptQueueSize(10).end().build();

        assertThat(configuration.getHttp().getPort(), is(8051));
        assertThat(configuration.getHttp().getHost(), is(of("host")));
        assertThat(configuration.getHttp().getAcceptQueueSize(), is(10));
    }
}
