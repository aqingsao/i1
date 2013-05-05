package com.thoughtworks.i1.commons.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.thoughtworks.i1.commons.config.builder.Builder;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.InputStream;

@XmlType
public class Configuration {

    public static <T extends Configuration> T read(InputStream configStream, Class<T> configurationType) throws IOException {
        return getMapper().readValue(configStream, configurationType);
    }

    public static String dump(Configuration configuration) throws JsonProcessingException {
        return getMapper().writeValueAsString(configuration);
    }

    public static ConfigurationBuilder config() {
        return new ConfigurationBuilder();
    }

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new GuavaModule());
        mapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()), new JacksonAnnotationIntrospector()));
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
        return mapper;
    }

    @NotNull
    private HttpConfiguration http;

    @NotNull
    private LoggingConfiguration logging;

    @NotNull
    private DatabaseConfiguration database;

    public Configuration() {
        http = config().http().build();
        logging = config().logging().build();
        database = config().database().build();
    }

    private Configuration(HttpConfiguration http, LoggingConfiguration logging, DatabaseConfiguration database) {
        this.http = http;
        this.logging = logging;
        this.database = database;
    }

    protected Configuration(Configuration configuration) {
        this.http = configuration.http;
        this.logging = configuration.logging;
    }

    @XmlElement
    public HttpConfiguration getHttp() {
        return http;
    }

    @XmlElement
    public LoggingConfiguration getLogging() {
        return logging;
    }

    @XmlElement
    public DatabaseConfiguration getDatabase() {
        return database;
    }

    public static class ConfigurationBuilder implements Builder<Configuration> {

        private HttpConfiguration.HttpConfigurationBuilder http = new HttpConfiguration.HttpConfigurationBuilder(this);

        private LoggingConfiguration.LoggingConfigurationBuilder logging = new LoggingConfiguration.LoggingConfigurationBuilder(this);

        private DatabaseConfiguration.DatabaseConfigurationBuilder database = new DatabaseConfiguration.DatabaseConfigurationBuilder(this);

        ConfigurationBuilder() {
        }

        public HttpConfiguration.HttpConfigurationBuilder http() {
            return http;
        }

        public LoggingConfiguration.LoggingConfigurationBuilder logging() {
            return logging;
        }

        @Override
        public Configuration build() {
            return new Configuration(http.build(), logging.build(), database.build());
        }

        public DatabaseConfiguration.DatabaseConfigurationBuilder database() {
            return database;
        }
    }
}
