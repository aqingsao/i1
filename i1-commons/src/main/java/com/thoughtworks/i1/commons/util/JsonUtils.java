package com.thoughtworks.i1.commons.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import java.io.IOException;

public class JsonUtils {
    public static <T> T fromJson(String json, Class<T> clz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        try {
            return objectMapper.readValue(json, clz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize json: " + e.getMessage(), e);
        }
    }

    public static String toJson(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
