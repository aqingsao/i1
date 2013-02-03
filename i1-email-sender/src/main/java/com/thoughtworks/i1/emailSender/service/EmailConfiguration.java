package com.thoughtworks.i1.emailSender.service;

import com.google.inject.name.Named;

import javax.inject.Singleton;

@Singleton
public class EmailConfiguration {
    @Named("smtpPort")
    public int smtpPort;
    @Named("mailServerHost")
    public String mailServerHost;

    @Named("authenticationNeeded")
    public boolean authenticationNeeded;
    @Named("authenticationUserName")
    public String authenticationUserName;
    @Named("authenticationPassword")
    public String authenticationPassword;

    public EmailConfiguration() {
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