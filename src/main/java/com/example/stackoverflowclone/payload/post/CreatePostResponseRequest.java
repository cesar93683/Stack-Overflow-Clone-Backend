package com.example.stackoverflowclone.payload.post;

import javax.validation.constraints.Size;

public class CreatePostResponseRequest {

    @Size(min = 1, max = 10)
    private String postId;

    @Size(min = 1, max = 500)
    private String content;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
