package com.example.rest.payload.data;

import javax.validation.constraints.Size;
import java.util.List;

public class CreateQuestionRequest {

    @Size(min = 3, max = 200)
    private String title;

    @Size(min = 3, max = 5000)
    private String content;

    private List<String> tags;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
