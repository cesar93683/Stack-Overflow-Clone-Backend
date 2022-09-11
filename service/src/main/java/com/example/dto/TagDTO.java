package com.example.dto;

import com.example.entity.Tag;

public class TagDTO {

    private int id;

    private String tag;

    private String description;

    private String numQuestions;

    public TagDTO(Tag tag, boolean onlyTag) {
        this.id = tag.getId();
        this.tag = tag.getTag();
        if (!onlyTag) {
            this.description = tag.getDescription();
            this.numQuestions = tag.getNumQuestions();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(String numQuestions) {
        this.numQuestions = numQuestions;
    }

}
