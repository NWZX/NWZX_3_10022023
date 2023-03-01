package com.chatop.estate.payloads.responses;

public class JwtResponse {
    private String jwt;

    public JwtResponse(String accessToken) {
        this.jwt = accessToken;
    }

    public String getToken() {
        return jwt;
    }

    public void setToken(String accessToken) {
        this.jwt = accessToken;
    }
}