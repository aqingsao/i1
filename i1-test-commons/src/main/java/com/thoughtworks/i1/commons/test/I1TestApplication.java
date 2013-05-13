package com.thoughtworks.i1.commons.test;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.thoughtworks.i1.commons.I1Application;
import com.thoughtworks.i1.commons.config.Configuration;
import com.thoughtworks.i1.commons.config.DatabaseConfiguration;

import java.net.URI;
import java.util.Collection;

public abstract class I1TestApplication extends I1Application{
    @Override
    protected Collection<? extends Module> getCustomizedModules() {
        return ImmutableList.of(new UriModule(getUri()));
    }

    public static class UriModule extends AbstractModule {
        private URI uri;
        public UriModule(URI uri){
            this.uri = uri;
        }

        @Override
        protected void configure() {
            bind(URI.class).toInstance(uri);
        }
    }
}
