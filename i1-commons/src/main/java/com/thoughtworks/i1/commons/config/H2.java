package com.thoughtworks.i1.commons.config;

import com.thoughtworks.i1.commons.config.DatabaseConfiguration;

import static com.thoughtworks.i1.commons.config.DatabaseConfiguration.DatabaseConfigurationBuilder.Setting;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Throwables.propagate;

public class H2 {
    public static final Setting driver = new DatabaseConfiguration.DatabaseConfigurationBuilder.Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            config.driver("org.h2.Driver");
        }
    };
    public static final Setting tempFileDB = new Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            try {
                config.url("jdbc:h2:" + File.createTempFile("i0-db-driver", ".db").getAbsolutePath());
            } catch (IOException e) {
                propagate(e);
            }
        }
    };

    public static final DatabaseConfiguration.DatabaseConfigurationBuilder.Setting fileDB(final String fileName) {
        return new DatabaseConfiguration.DatabaseConfigurationBuilder.Setting() {
            @Override
            public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
                config.url("jdbc:h2:" + fileName);
            }
        };
    }

    public static final Setting privateMemoryDB = new Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            config.url("jdbc:h2:mem");
        }
    };


    public static Setting compatible(final String mode) {
        return new Setting() {
            @Override
            public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
                config.appendToUrl(";MODE=" + mode);
            }
        };
    }
}
