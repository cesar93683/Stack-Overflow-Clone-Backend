package com.example.service;

import com.example.dto.CommentDTO;
import com.example.exceptions.ServiceException;

public interface CommentService {
    CommentDTO createQuestionComment(String content, int questionId, int userId) throws ServiceException;

    CommentDTO createAnswerComment(String content, int answerId, int userId) throws ServiceException;

    void deleteComment(int commentId, int userId) throws ServiceException;

    void voteComment(int userId, int commentId, String vote) throws ServiceException;
}
