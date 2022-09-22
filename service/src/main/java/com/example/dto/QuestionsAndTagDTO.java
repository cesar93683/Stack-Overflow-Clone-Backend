package com.example.dto;

public class QuestionsAndTagDTO {

    private QuestionsDTO questions;
    private TagDTO tag;

    public QuestionsAndTagDTO(QuestionsDTO questions, TagDTO tag) {
        this.questions = questions;
        this.tag = tag;
    }

    public QuestionsDTO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsDTO questions) {
        this.questions = questions;
    }

    public TagDTO getTag() {
        return tag;
    }

    public void setTag(TagDTO tag) {
        this.tag = tag;
    }
}
