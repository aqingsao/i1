package com.thoughtworks.i1.emailSender.service;

import com.google.inject.name.Named;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EmailConfiguration {
    public int smtpPort;
    public String mailServerHost;

    public boolean authenticationNeeded;
    public String authenticationUserName;
    public String authenticationPassword;

    @Inject
    public EmailConfiguration(@Named("smtpPort") int smtpPort, @Named("mailServerHost") String mailServerHost,
                              @Named("authenticationNeeded") boolean authenticationNeeded,
                              @Named("authenticationUserName") String authenticationUserName,
                              @Named("authenticationPassword")String authenticationPassword) {
        this.smtpPort = smtpPort;
        this.mailServerHost = mailServerHost;
        this.authenticationNeeded = authenticationNeeded;
        this.authenticationUserName = authenticationUserName;
        this.authenticationPassword = authenticationPassword;
    }

    public void setMailServerPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getAuthenticationUserName() {
        return authenticationUserName;
    }

    public void setAuthenticationUserName(String authenticationUserName) {
        this.authenticationUserName = authenticationUserName;
    }

    public String getAuthenticationPassword() {
        return authenticationPassword;
    }

    public void setAuthenticationPassword(String authenticationPassword) {
        this.authenticationPassword = authenticationPassword;
    }

    public void setAuthenticationNeeded(Boolean authenticationNeeded) {
        this.authenticationNeeded = authenticationNeeded;
    }

    public boolean isAuthenticationRequired() {
        return authenticationNeeded;
    }

    public int getMailServerPort() {
        return smtpPort;
    }
}