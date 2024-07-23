package com.hotworx.requestEntity;

import java.io.Serializable;
public class MainProfileResultModel implements Serializable {
    private ViewProfileResponse data;
    private String token;
    private String two_factor;

    public ViewProfileResponse getData() {
        return data;
    }

    public void setData(ViewProfileResponse data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTwoFactor() {
        return two_factor;
    }

    public void setTwoFactor(String two_factor) {
        this.two_factor = two_factor;
    }
}