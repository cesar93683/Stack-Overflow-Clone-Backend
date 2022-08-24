package com.example.rest.payload.post;

import javax.validation.constraints.Size;

public class VoteRequest {

    @Size(min = 1, max = 9)
    private String action;

    @Size(min = 1, max = 10)
    private String id;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
