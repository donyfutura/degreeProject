package com.example.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest {

    @JsonProperty("e_mail")
    private String email;

    private String password;

    private String name;

    private String captcha;

    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String name, String captcha, String captchaSecret) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.captcha = captcha;
        this.captchaSecret = captchaSecret;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaSecret() {
        return captchaSecret;
    }

    public void setCaptchaSecret(String captchaSecret) {
        this.captchaSecret = captchaSecret;
    }
}
