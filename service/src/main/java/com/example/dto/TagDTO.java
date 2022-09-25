package com.example.dto;

import com.example.entity.Tag;

public class TagDTO {

    private String tag;
    private String description;
    private int numQuestions;

    public TagDTO() {
    }

    public TagDTO(Tag tag, boolean onlyTag) {
        this.tag = tag.getTag();
        if (!onlyTag) {
            this.description = tag.getDescription();
            this.numQuestions = tag.getNumQuestions();
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

}
