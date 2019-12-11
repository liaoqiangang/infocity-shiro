package com.infocity.dto;

/**
 * @author liaoqiangang
 * @date 2019/12/11 10:05 AM
 * @desc
 * @lastModifier
 * @lastModifyTime
 **/
public class User {

    private String username;
    private String password;

    private boolean rememberme;

    public boolean isRememberme() {
        return rememberme;
    }

    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
