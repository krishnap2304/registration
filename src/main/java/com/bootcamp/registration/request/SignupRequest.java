package com.bootcamp.registration.request;

import java.util.Set;

public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
    private  String captchaResp;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    private String newPassword;


    public String getCaptchaResp() {
        return captchaResp;
    }

    public void setCaptchaResp(String captchaResp) {
        this.captchaResp = captchaResp;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", captchaResp="+captchaResp+
                '}';
    }
}
