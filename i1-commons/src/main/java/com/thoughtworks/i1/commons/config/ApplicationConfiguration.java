package com.thoughtworks.i1.commons.config;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import com.thoughtworks.i1.commons.config.builder.Builder;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType
public class ApplicationConfiguration{

    public static final boolean DEFAULT_SHARE_NOTHING = true;
    public static final String DEFAULT_SCANNING_PACKAGE = "com.thoughtworks.i1";
    public static final String DEFAULT_API_PREFIX = "/api/*";

    private String contextPath;

    private boolean shareNothing = DEFAULT_SHARE_NOTHING;
    private List<Module> modules = Lists.newArrayList();
    private String apiPrefix = DEFAULT_API_PREFIX;
    private String scanningPackage = DEFAULT_SCANNING_PACKAGE;
    private String[] propertyFiles = new String[0];

    public ApplicationConfiguration() {
    }

    public ApplicationConfiguration(String contextPath, boolean shareNothing, String apiPrefix, String scanningPackage, String... propertyFiles) {
        this.contextPath = contextPath;
        this.shareNothing = shareNothing;
        this.apiPrefix = apiPrefix;
        this.scanningPackage = scanningPackage;
        this.propertyFiles = propertyFiles;
    }

    public String getContextPath() {
        return contextPath;
    }

    public boolean isShareNothing() {
        return shareNothing;
    }

    public Module[] getModules() {
        return modules.toArray(new Module[0]);
    }

    public String getApiPrefix() {
        return apiPrefix;
    }

    public String getScanningPackage() {
        return scanningPackage;
    }

    public String[] getPropertyFiles() {
        return propertyFiles;
    }

    public static class ApplicationConfigurationBuilder implements Builder<ApplicationConfiguration> {
        private Configuration.ConfigurationBuilder parent;
        private String contextPath;
        private boolean shareNothing = DEFAULT_SHARE_NOTHING;
        private String apiPrefix = DEFAULT_API_PREFIX;
        private String scanningPackage = DEFAULT_SCANNING_PACKAGE;
        private String[] propertyFiles = new String[0];

        public ApplicationConfigurationBuilder(Configuration.ConfigurationBuilder parent) {
            this.parent = parent;
        }

        public Configuration.ConfigurationBuilder end() {
            return parent;
        }

        public ApplicationConfiguration.ApplicationConfigurationBuilder contextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        public ApplicationConfiguration.ApplicationConfigurationBuilder shareNothing(boolean shareNothing) {
            this.shareNothing = shareNothing;
            return this;
        }

        public ApplicationConfigurationBuilder jerseyServletModule(String apiPrefix, String scanningPackage) {
            this.apiPrefix = apiPrefix;
            this.scanningPackage = scanningPackage;
            return this;
        }

        public ApplicationConfigurationBuilder propertyFiles(String... propertyFiles) {
            this.propertyFiles = propertyFiles;
            return this;
        }

        @Override
        public ApplicationConfiguration build() {
            return new ApplicationConfiguration(contextPath, shareNothing, apiPrefix, scanningPackage, propertyFiles);
        }
    }
}
