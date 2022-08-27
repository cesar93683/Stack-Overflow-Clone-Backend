package com.example.rest.payload.auth;

import com.example.rest.payload.GenericResponse;

public class LoginResponse extends GenericResponse {

    String token;
    int userId;

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
