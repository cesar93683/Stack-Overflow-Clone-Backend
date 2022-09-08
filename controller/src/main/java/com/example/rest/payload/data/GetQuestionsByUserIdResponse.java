package com.example.rest.payload.data;

import com.example.dto.QuestionsDTO;
import com.example.dto.UserDTO;
import com.example.rest.payload.GenericResponse;

public class GetQuestionsByUserIdResponse extends GenericResponse {

    private UserDTO user;
    private QuestionsDTO questions;

    public GetQuestionsByUserIdResponse(int code, UserDTO user, QuestionsDTO questions) {
        super(code);
        this.user = user;
        this.questions = questions;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public QuestionsDTO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionsDTO questions) {
        this.questions = questions;
    }
}
