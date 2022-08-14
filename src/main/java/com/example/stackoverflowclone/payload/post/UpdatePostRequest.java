package com.example.stackoverflowclone.payload.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatePostRequest {

    @NotBlank
    @Size(max = 500)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
