package com.example.rest.payload.post;

import javax.validation.constraints.Size;

public class CreatePostRequest {

    private String title;

    @Size(min = 1, max = 500)
    private String content;

    private String postResponseId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostResponseId() {
        return postResponseId;
    }

    public void setPostResponseId(String postResponseId) {
        this.postResponseId = postResponseId;
    }

}
