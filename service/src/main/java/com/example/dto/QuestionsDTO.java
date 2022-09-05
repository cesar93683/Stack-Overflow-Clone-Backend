package com.example.dto;

import java.util.List;

public class QuestionsDTO {

    private int totalPages;
    private List<QuestionDTO> questions;

    public QuestionsDTO(int totalPages, List<QuestionDTO> questions) {
        this.questions = questions;
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
