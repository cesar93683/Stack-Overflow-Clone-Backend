package com.example.rest.payload;

public class GenericResponse {

    private int code;

    public GenericResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}