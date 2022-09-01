package com.example.rest.payload.data;

import javax.validation.constraints.Size;

public class CreateCommentRequest {

    @Size(min = 3, max = 100)
    private String content;

    @Size(min = 1, max = 10)
    private String id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
