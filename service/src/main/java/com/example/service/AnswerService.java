package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.exceptions.ServiceException;

import java.util.List;

public interface AnswerService {
    List<AnswerDTO> getAnswers(int questionId, int page, boolean sortedByVotes, int userId);

    List<AnswerDTO> getAnswersByUserId(int userId, int page, boolean sortedByVotes, int userIdIfExists);

    AnswerDTO createAnswer(String content, int userId, int questionId) throws ServiceException;

    void updateAnswer(int answerId, String content, int userId) throws ServiceException;

    void deleteAnswer(int answerId, int userId) throws ServiceException;

    void voteAnswer(int userId, int answerId, String vote) throws ServiceException;

    CommentDTO createAnswerComment(String content, int answerId, int userId) throws ServiceException;
}
