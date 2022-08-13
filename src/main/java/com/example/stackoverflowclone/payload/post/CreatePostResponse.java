package com.example.stackoverflowclone.payload.post;

import com.example.stackoverflowclone.payload.GenericResponse;

public class CreatePostResponse extends GenericResponse {

    int postId;

    public CreatePostResponse(int code, String message, int postId) {
        super(code, message);
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
