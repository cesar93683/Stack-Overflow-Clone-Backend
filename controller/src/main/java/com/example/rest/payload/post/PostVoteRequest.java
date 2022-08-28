package com.example.rest.payload.post;

import javax.validation.constraints.Size;

public class PostVoteRequest {

    @Size(min = 1, max = 10)
    private String postId;

    @Size(min = 1, max = 9)
    private String action;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
