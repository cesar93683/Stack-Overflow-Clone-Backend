package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.dto.QuestionDTO;
import com.example.exceptions.QuestionAndAnswerException;

import java.util.List;

public interface QuestionAndAnswerService {
    List<QuestionDTO> getQuestions(int page, boolean sortByVotes, int userId);

    List<QuestionDTO> getQuestionsByUserId(int userId, int page, boolean sortedByVotes, int userIdIfExists);

    QuestionDTO getQuestion(int questionId, int userId) throws QuestionAndAnswerException;

    QuestionDTO createQuestion(String title, String content, int userId) throws QuestionAndAnswerException;

    void updateQuestion(int questionId, String content, int userId) throws QuestionAndAnswerException;

    void deleteQuestion(int questionId, int userId) throws QuestionAndAnswerException;

    void voteQuestion(int userId, int questionId, String vote) throws QuestionAndAnswerException;

    List<AnswerDTO> getAnswers(int answerId, int page, boolean sortedByVotes, int userId);

    AnswerDTO createAnswer(String content, int userId, int questionId) throws QuestionAndAnswerException;

    void updateAnswer(int answerId, String content, int userId) throws QuestionAndAnswerException;

    void deleteAnswer(int answerId, int userId) throws QuestionAndAnswerException;

    void voteAnswer(int userId, int answerId, String vote) throws QuestionAndAnswerException;

    CommentDTO createQuestionComment(String content, int questionId, int userId) throws QuestionAndAnswerException;

    CommentDTO createAnswerComment(String content, int answerId, int userId) throws QuestionAndAnswerException;

    void deleteComment(int commentId, int userId) throws QuestionAndAnswerException;

    void voteComment(int userId, int commentId, String vote) throws QuestionAndAnswerException;
}
