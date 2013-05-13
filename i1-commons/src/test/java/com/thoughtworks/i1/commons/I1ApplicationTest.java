package com.thoughtworks.i1.commons;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;
import com.thoughtworks.i1.commons.server.Embedded;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class I1ApplicationTest {
    private Embedded server;

    @After
    public void after() {
        if(server != null){
            server.stop();
        }
    }

    @Test
    public void should_use_api_as_prefix_in_default() {
        server = new I1TestApplication().start(false);

        ClientResponse clientResponse = get("/", "/api/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_override_api_prefix_when_given() {
        server = new I1TestApplication().setApiPrefix("/test/*").start(false);

        ClientResponse clientResponse = get("/", "/test/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_use_root_context_in_default() {
        server = new I1TestApplication().start(false);

        ClientResponse clientResponse = get("/", "/api/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_override_context_path_when_given() {
        server = new I1TestApplication().setContextPath("/test").start(false);

        ClientResponse clientResponse = get("/test", "/api/outer");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("outer"));
    }

    @Test
    public void should_only_load_inner_resources_when_given_scanning_package() {
        server = new I1TestApplication().setScanningPackage("com.thoughtworks.i1.commons.inner").start(false);

        ClientResponse clientResponse = get("/", "/api/inner");
        assertThat(clientResponse.getClientResponseStatus(), is(ClientResponse.Status.OK));
        assertThat(clientResponse.getEntity(String.class), is("inner"));

        assertThat(get("/", "/api/outer").getClientResponseStatus(), is(ClientResponse.Status.NOT_FOUND));
    }

    @Ignore
    public void should_load_property_file_when_given() {
        server = new I1TestApplication().setPropertyFiles("application.property").start(false);

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
        private String contextPath = "/";
        private String[] propertyFiles = new String[0];
        private String apiPrefix = "/api/*";
        private String scanningPackage = "com.thoughtworks.i1";

        public I1TestApplication() {
            super();
        }

        public I1TestApplication setContextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        public I1TestApplication setApiPrefix(String apiPrefix) {
            this.apiPrefix = apiPrefix;
            return this;
        }

        public I1TestApplication setScanningPackage(String scanningPackage) {
            this.scanningPackage = scanningPackage;
            return this;
        }

        public I1TestApplication setPropertyFiles(String... propertyFiles) {
            this.propertyFiles = propertyFiles;
            return this;
        }


        @Override
        protected Configuration defaultConfiguration() {
            return Configuration.config()
                    .app().contextPath(contextPath).jerseyServletModule(apiPrefix, scanningPackage).propertyFiles(propertyFiles).end()
                    .http().port(8051).end()
                    .database().persistUnit("domain").with(DatabaseConfiguration.H2.driver, DatabaseConfiguration.H2.tempFileDB, DatabaseConfiguration.H2.compatible("ORACLE"), DatabaseConfiguration.Hibernate.dialect("Oracle10g"), DatabaseConfiguration.Hibernate.showSql).user("sa").password("").end()
                    .build();
        }
    }
}
