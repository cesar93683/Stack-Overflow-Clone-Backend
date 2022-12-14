package com.example.service;

import com.example.dto.*;
import com.example.exceptions.ServiceException;

import java.util.List;

public interface QuestionService {
    QuestionsDTO getQuestions(int page, boolean sortedByVotes);

    QuestionsDTO getQuestionsByUserId(int userId, int page, boolean sortedByVotes);

    QuestionsDTO getQuestionsAnsweredByUserId(int userId, int page, boolean sortedByVotes);

    QuestionDTO getQuestion(int questionId, int userId) throws ServiceException;

    QuestionDTO createQuestion(String title, String content, List<String> tagTypes, int userId) throws ServiceException;

    void updateQuestion(int questionId, String content, List<String> tagTypes, int userId) throws ServiceException;

    void deleteQuestion(int questionId, int userId) throws ServiceException;

    void voteQuestion(int userId, int questionId, int vote) throws ServiceException;

    CommentDTO createComment(String content, int questionId, int userId) throws ServiceException;

    List<TagDTO> getTags();

    QuestionsAndTagDTO getQuestionByTag(String tag, int page, boolean sortedByVotes);
}
