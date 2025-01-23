package com.blu.auth;

public class LoginResponse {
    private String token;

    private long expiresIn;

    public LoginResponse() {
        this.token = "";
        this.expiresIn = 0;
    }
    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    // Getters and setters...
}