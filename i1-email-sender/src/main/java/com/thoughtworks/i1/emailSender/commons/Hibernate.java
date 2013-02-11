package com.thoughtworks.i1.emailSender.commons;

import static com.thoughtworks.i1.emailSender.commons.DatabaseConfiguration.DatabaseConfigurationBuilder.*;

public class Hibernate {
    public static Setting dialect(final String name) {
        return new Setting() {
            @Override
            public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
                config.property("hibernate.dialect", "org.hibernate.dialect." + name + "Dialect");
            }
        };
    }

    public static Setting createDrop = new Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            config.property("hibernate.hbm2ddl.auto", "create-drop");
        }
    };

    public static Setting validate = new Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            config.property("hibernate.hbm2ddl.auto", "validate");
        }
    };

    public static Setting create = new Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            config.property("hibernate.hbm2ddl.auto", "create");
        }
    };

    public static Setting showSql = new Setting() {
        @Override
        public void set(DatabaseConfiguration.DatabaseConfigurationBuilder config) {
            config.property("hibernate.show_sql", "true");
        }
    };
}
