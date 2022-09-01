package com.example.rest.payload.data;

import javax.validation.constraints.Size;

public class CreateQuestionRequest {

    @Size(min = 3, max = 50)
    private String title;

    @Size(min = 3, max = 500)
    private String content;

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

}
