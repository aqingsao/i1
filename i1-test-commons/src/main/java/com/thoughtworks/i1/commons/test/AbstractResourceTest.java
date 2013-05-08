package com.thoughtworks.i1.commons.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.thoughtworks.i1.commons.util.JsonUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResourceTest {
    @Inject
    private EntityManager entityManager;

    @Inject
    private URI baseUri;

    public AbstractResourceTest() {
    }

    protected URI uri(String path) {
        return UriBuilder.fromUri(baseUri).path(path).build();
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected <T> T persist(T object) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(object);
        transaction.commit();
        return object;
    }

    protected <T> T remove(T object) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(object);
        transaction.commit();
        return object;
    }

    protected <T> void removeAll(Class<T> entityClass) {
        EntityManager entityManager = getEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        TypedQuery<T> query = entityManager.createQuery("select o from " + entityClass.getName() + " o", entityClass);
        for (T t : query.getResultList()) {
            entityManager.remove(t);
        }
        transaction.commit();
    }
    protected ClientResponse get(String path) {
        WebResource webResource = Client.create().resource(uri(path));

        return webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
    }


    protected String toJson(Object object) {
        return JsonUtils.toJson(object);
    }

    protected <T> T fromJson(String json, Class<T> entityClass) {
        return JsonUtils.fromJson(json, entityClass);
    }


    protected boolean containsJsonNode(Iterable<JsonNode> jsonNodes, String name, Object value) {
        for (JsonNode node : jsonNodes) {
            if (getValue(node, name, value).equals(value)) {
                return true;
            }
        }
        return false;
    }

    protected JsonNode getJsonNodeBy(Iterable<JsonNode> jsonNodes, String name, Object value) {
        List<Object> list = new ArrayList<>();
        for (JsonNode node : jsonNodes) {
            Object actual = getValue(node, name, value);
            list.add(actual);
            if (actual.equals(value)) {
                return node;
            }
        }
        throw new RuntimeException(String.format("Cannot find %s in %s", value, list));
    }

    private Object getValue(JsonNode node, String name, Object value) {
        JsonNode jsonNode = node.get(name);
        if (value instanceof Long) {
            return jsonNode.asLong();
        }
        if (value instanceof Integer) {
            return jsonNode.asInt();
        }
        if (value instanceof Boolean) {
            return jsonNode.asBoolean();
        }
        if (value instanceof Double) {
            jsonNode.asDouble();
        }
        return jsonNode.asText();
    }
}