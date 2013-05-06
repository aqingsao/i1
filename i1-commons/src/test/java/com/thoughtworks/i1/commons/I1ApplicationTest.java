package com.thoughtworks.i1.commons;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.server.Embedded;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class I1ApplicationTest {
    private Configuration configuration;
    private Embedded server;

    @Before
    public void before() {
        configuration = Configuration.config()
                .http().port(8051).end()
                .database().with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                .build();
    }

    @After
    public void after(){
       server.stop();
    }

    @Test
    public void should_use_api_as_prefix_in_default() {
        server = new I1TestApplication().runInEmbeddedJetty(false);

        ClientResponse clientResponse = get("/", "/api/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_override_api_prefix_when_given() {
        server = new I1TestApplication() {
            @Override
            protected String getApiPrefix() {
                return "/test/*";
            }
        }.runInEmbeddedJetty(false);

        ClientResponse clientResponse = get("/", "/test/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_use_root_context_in_default() {
        server = new I1TestApplication().runInEmbeddedJetty(false);

        ClientResponse clientResponse = get("/", "/api/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_override_context_path_when_given() {
        server = new I1TestApplication(){
            @Override
            protected String getContextPath() {
                return "/test";
            }
        }.runInEmbeddedJetty(false);

        ClientResponse clientResponse = get("/test", "/api/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_only_load_inner_resources_when_given_scanning_package() {
        server = new I1TestApplication(){
            @Override
            protected String getScanningPackage() {
                return "com.thoughtworks.i1.commons.inner";
            }
        }.runInEmbeddedJetty(false);

        ClientResponse clientResponse = get("/", "/api/inner");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("inner"));

        assertThat(get("/", "/api/outer").getClientResponseStatus(), is(ClientResponse.Status.NOT_FOUND));
    }

    @Test
    public void should_load_property_file_when_given() {
        server = new I1TestApplication(){
            @Override
            protected String[] getPropertyFiles() {
                return new String[]{"application.property"};
            }
        }.runInEmbeddedJetty(false);

        ClientResponse clientResponse = get("/", "/api/inner/property");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("bar"));
    }

    public static ClientResponse get(String contextPath, String path) {
        return Client.create().resource(uri(contextPath)).path(path)
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
    }

    private static String uri(String contextPath) {
        return "http://localhost:8051" + contextPath;
    }

    private class I1TestApplication extends I1Application {
        @Override
        protected Configuration defaultConfiguration() {
            return configuration;
        }
    }
}
