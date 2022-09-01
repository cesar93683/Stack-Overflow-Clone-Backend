package com.example.rest.payload.data;

import com.example.rest.payload.GenericResponse;

public class CreateCommentResponse extends GenericResponse {

    int commentId;

    public CreateCommentResponse(int code, int commentId) {
        super(code);
        this.commentId = commentId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
