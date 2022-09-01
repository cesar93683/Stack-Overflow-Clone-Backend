package com.example.rest.payload.data;

import javax.validation.constraints.Size;

public class CreateAnswerRequest {

    @Size(min = 3, max = 500)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
