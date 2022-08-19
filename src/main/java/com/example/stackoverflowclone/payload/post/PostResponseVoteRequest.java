package com.example.stackoverflowclone.payload.post;

import javax.validation.constraints.Size;

public class PostResponseVoteRequest {

    @Size(min = 1, max = 9)
    private String action;

    @Size(min = 1, max = 10)
    private String postResponseId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPostResponseId() {
        return postResponseId;
    }

    public void setPostResponseId(String postResponseId) {
        this.postResponseId = postResponseId;
    }
}
