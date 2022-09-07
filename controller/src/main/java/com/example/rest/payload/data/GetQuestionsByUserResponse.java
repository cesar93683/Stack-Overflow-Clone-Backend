package com.example.rest.payload.data;

import com.example.dto.QuestionDTO;
import com.example.dto.UserDTO;
import com.example.rest.payload.GenericResponse;

import java.util.List;

public class GetQuestionsByUserResponse extends GenericResponse {

    private UserDTO user;
    private List<QuestionDTO> questions;
    private int totalPages;

    public GetQuestionsByUserResponse(int code, UserDTO user, List<QuestionDTO> questions, int totalPages) {
        super(code);
        this.user = user;
        this.questions = questions;
        this.totalPages = totalPages;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
