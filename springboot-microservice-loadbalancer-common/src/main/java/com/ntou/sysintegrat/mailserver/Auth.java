package com.ntou.sysintegrat.mailserver;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

class Auth extends Authenticator {

    private final String userName;
    private final String password;

    public Auth(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
