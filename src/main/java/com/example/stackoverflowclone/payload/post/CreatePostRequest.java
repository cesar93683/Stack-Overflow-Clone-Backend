package com.example.stackoverflowclone.payload.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreatePostRequest {

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 500)
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
