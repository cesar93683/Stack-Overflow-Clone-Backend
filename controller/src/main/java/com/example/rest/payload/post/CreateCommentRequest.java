package com.example.rest.payload.post;

import javax.validation.constraints.Size;

public class CreateCommentRequest {

    @Size(min = 3, max = 100)
    private String content;

    @Size(min = 1, max = 10)
    private String postId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

}
