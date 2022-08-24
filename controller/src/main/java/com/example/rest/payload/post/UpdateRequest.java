package com.example.rest.payload.post;

import javax.validation.constraints.Size;

public class UpdateRequest {

    @Size(min = 1, max = 500)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
