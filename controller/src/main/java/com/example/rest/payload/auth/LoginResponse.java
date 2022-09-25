package com.example.rest.payload.auth;

import com.example.rest.payload.GenericResponse;

public class LoginResponse extends GenericResponse {

    private String token;
    private int userId;

    public LoginResponse() {
    }

    public LoginResponse(int code, String token, int userId) {
        super(code);
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
