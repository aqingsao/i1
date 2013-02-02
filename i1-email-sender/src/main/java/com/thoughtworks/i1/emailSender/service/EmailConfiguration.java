package com.thoughtworks.i1.emailSender.service;

public class EmailConfiguration {

    public final int MAIL_PORT_DEFAULT_VALUE = 25;

    public int smtpPort = MAIL_PORT_DEFAULT_VALUE;

    public String mailServerHost = "localhost";

    /**
     * If authentication is enabled (true), you must enter values ​​for properties:
     * authenticationPassword, authenticationUserName
     */
    public boolean authenticationNeeded = false;

    public String authenticationUserName = null;

    public String authenticationPassword = null;

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

    boolean isAuthenticationRequired() {
        return authenticationNeeded;
    }

    int getMailServerPort() {
        return smtpPort;
    }
}