package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.exceptions.ServiceException;

public interface AnswerService {
    AnswerDTO createAnswer(String content, int userId, int questionId) throws ServiceException;

    void updateAnswer(int answerId, String content, int userId) throws ServiceException;

    void deleteAnswer(int answerId, int userId) throws ServiceException;

    void voteAnswer(int userId, int answerId, int vote) throws ServiceException;

    CommentDTO createComment(String content, int answerId, int userId) throws ServiceException;
}
