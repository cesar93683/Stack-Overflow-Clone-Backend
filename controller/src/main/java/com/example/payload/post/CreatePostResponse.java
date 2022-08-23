package com.example.payload.post;

import com.example.payload.GenericResponse;

public class CreatePostResponse extends GenericResponse {

    int postId;

    public CreatePostResponse(int code, int postId) {
        super(code);
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
