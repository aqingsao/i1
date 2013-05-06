package com.thoughtworks.i1.commons;

import com.thoughtworks.i1.commons.config.Configuration;

public abstract class AbstractApplication {
    public Configuration getConfiguration(){
        return defaultConfiguration();
    }

    protected abstract Configuration defaultConfiguration();
}
