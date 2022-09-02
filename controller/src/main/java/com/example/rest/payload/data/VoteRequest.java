package com.example.rest.payload.data;

import javax.validation.constraints.Size;

public class VoteRequest {

    @Size(min = 1, max = 1)
    private String vote;

    @Size(min = 1, max = 10)
    private String id;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
