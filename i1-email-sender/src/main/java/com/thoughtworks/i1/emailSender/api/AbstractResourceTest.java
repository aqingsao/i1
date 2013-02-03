package com.thoughtworks.i1.emailSender.api;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public abstract class AbstractResourceTest{
    protected static final URI BASE_URI = baseURI();
    protected static HttpServer httpServer = createHttpServer();

    private static URI baseURI() {
        return UriBuilder.fromUri("http://localhost/").port(9998).build();
    }

    private static HttpServer createHttpServer() {
        ResourceConfig rc = new PackagesResourceConfig("com.thoughtworks.i1.emailSender");
        try {
            return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public AbstractResourceTest(){
    }

    protected URI uri(String path) {
        return UriBuilder.fromUri(BASE_URI).path(path).build();
    }
}
