package com.example.service;

import com.example.dto.CommentDTO;
import com.example.dto.QuestionDTO;
import com.example.exceptions.ServiceException;

import java.util.List;

public interface QuestionService {
    List<QuestionDTO> getQuestions(int page, boolean sortByVotes);

    List<QuestionDTO> getQuestionsByUserId(int userId, int page, boolean sortedByVotes);

    List<QuestionDTO> getQuestionsAnsweredByUserId(int userId, int page, boolean sortedByVotes);

    QuestionDTO getQuestion(int questionId, int userId) throws ServiceException;

    QuestionDTO createQuestion(String title, String content, int userId) throws ServiceException;

    void updateQuestion(int questionId, String content, int userId) throws ServiceException;

    void deleteQuestion(int questionId, int userId) throws ServiceException;

    void voteQuestion(int userId, int questionId, int vote) throws ServiceException;

    CommentDTO createComment(String content, int questionId, int userId) throws ServiceException;
}
