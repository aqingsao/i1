package com.thoughtworks.i1.emailSender.commons;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Iterables.toArray;

public class DatabaseConfiguration {
    public static DatabaseConfigurationBuilder database() {
        return new DatabaseConfigurationBuilder();
    }

    @NotNull
    private String driver;
    @NotNull
    private String url;
    @NotNull
    private String password;
    @NotNull
    private String user;
    @NotNull
    private Map<String, String> properties = ImmutableMap.of();
    private Optional<MigrationConfiguration> migration = absent();

    public DatabaseConfiguration() {
    }

    DatabaseConfiguration(String driver, String url, String password, String user, Map<String, String> properties, Optional<MigrationConfiguration> migration) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.user = user;
        this.properties = properties == null ? ImmutableMap.<String, String>of() : properties;
        this.migration = migration;
    }

    @XmlElement
    public String getDriver() {
        return driver;
    }

    @XmlElement
    public String getUrl() {
        return url;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    @XmlElement
    public String getUser() {
        return user;
    }

    @XmlElement
    public Map<String, String> getProperties() {
        return properties;
    }

    @XmlElement
    public Optional<MigrationConfiguration> getMigration() {
        return migration;
    }

    public Properties toProperties() {
        Properties properties = new Properties();

        properties.put("javax.persistence.jdbc.driver", driver);
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", user);
        properties.put("javax.persistence.jdbc.password", password);
        properties.putAll(this.properties);
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseConfiguration that = (DatabaseConfiguration) o;

        if (!driver.equals(that.driver)) return false;
        if (!password.equals(that.password)) return false;
        if (!properties.equals(that.properties)) return false;
        if (!url.equals(that.url)) return false;
        if (!user.equals(that.user)) return false;
        if (!migration.equals(that.migration)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, password, user, properties, migration);
    }

    @XmlType
    public static class MigrationConfiguration {
        private boolean auto = true;
        private String[] locations = new String[]{"com/thoughtworks/i0/jersey/api/p2/migration"};
        private Map<String, String> placeholders = ImmutableMap.of();

        public MigrationConfiguration() {
        }

        public MigrationConfiguration(boolean auto, String[] locations, Map<String, String> placeholders) {
            this.auto = auto;
            this.locations = locations;
            this.placeholders = placeholders;
        }

        @XmlElement
        public boolean isAuto() {
            return auto;
        }

        @XmlElement
        public String[] getLocations() {
            return locations;
        }

        @XmlElement
        public Map<String, String> getPlaceholders() {
            return placeholders;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MigrationConfiguration that = (MigrationConfiguration) o;

            if (auto != that.auto) return false;
            if (!Arrays.equals(locations, that.locations)) return false;
            if (!placeholders.equals(that.placeholders)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (auto ? 1 : 0);
            result = 31 * result + Arrays.hashCode(locations);
            result = 31 * result + placeholders.hashCode();
            return result;
        }
    }

    public static class DatabaseConfigurationBuilder implements Builder<DatabaseConfiguration> {
        private String driver;
        private String url;
        private StringBuffer urlAppendix = new StringBuffer();
        private String password;
        private String user;
        private ImmutableMap.Builder<String, String> properties = ImmutableMap.builder();
        private OptionalBuilder<MigrationConfigurationBuilder, MigrationConfiguration>
                migration = new OptionalBuilder<>(new MigrationConfigurationBuilder());

        DatabaseConfigurationBuilder() {
        }

        public DatabaseConfigurationBuilder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public DatabaseConfigurationBuilder url(String url) {
            this.url = url;
            return this;
        }

        public DatabaseConfigurationBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DatabaseConfigurationBuilder user(String user) {
            this.user = user;
            return this;
        }

        public DatabaseConfigurationBuilder appendToUrl(String appendix) {
            this.urlAppendix.append(appendix);
            return this;
        }

        public DatabaseConfigurationBuilder property(String key, String value) {
            properties.put(key, value);
            return this;
        }

        public DatabaseConfigurationBuilder with(Setting... settings) {
            for (Setting setting : settings) {
                setting.set(this);
            }
            return this;
        }

        public MigrationConfigurationBuilder migration() {
            return migration.builder();
        }

        public static abstract class Setting {
            public static Setting and(final Setting... settings) {
                return new Setting() {
                    @Override
                    public void set(DatabaseConfigurationBuilder config) {
                        for (Setting setting : settings) setting.set(config);
                    }
                };
            }

            public abstract void set(DatabaseConfigurationBuilder config);
        }

        public DatabaseConfiguration build() {
            return new DatabaseConfiguration(driver, url, password, user, properties.build(), migration.build());
        }

        public class MigrationConfigurationBuilder implements Builder<DatabaseConfiguration.MigrationConfiguration> {
            private boolean auto = true;
            private ImmutableSet.Builder<String> locations = ImmutableSet.builder();
            private ImmutableMap.Builder<String, String> placeholders = ImmutableMap.builder();

            public MigrationConfigurationBuilder auto(boolean auto) {
                this.auto = auto;
                return this;
            }

            public MigrationConfigurationBuilder locations(String... locations) {
                this.locations.add(locations);
                return this;
            }

            public MigrationConfigurationBuilder placeholder(String placeholder, String value) {
                placeholders.put(placeholder, value);
                return this;
            }

            public DatabaseConfigurationBuilder end() {
                return DatabaseConfigurationBuilder.this;
            }

            public MigrationConfiguration build() {
                return new MigrationConfiguration(auto, toArray(locations.build(), String.class), placeholders.build());
            }
        }
    }
}
