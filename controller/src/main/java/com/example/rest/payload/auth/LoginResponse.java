package com.example.rest.payload.auth;

import com.example.rest.payload.GenericResponse;

public class LoginResponse extends GenericResponse {

    String token;

    public LoginResponse(int code, String token) {
        super(code);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
