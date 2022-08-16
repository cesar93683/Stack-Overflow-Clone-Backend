package com.example.stackoverflowclone.payload.post;

import javax.validation.constraints.NotBlank;

public class VotePostRequest {

    @NotBlank
    private String action;

    @NotBlank
    private String postId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
