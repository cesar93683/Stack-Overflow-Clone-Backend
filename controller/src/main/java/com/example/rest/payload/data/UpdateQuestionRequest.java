package com.example.rest.payload.data;

import javax.validation.constraints.Size;
import java.util.List;

public class UpdateQuestionRequest {

    @Size(min = 3, max = 500)
    private String content;

    private List<String> tags;

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
