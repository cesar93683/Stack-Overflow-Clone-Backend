package com.example.service;

import com.example.exceptions.ServiceException;

public interface CommentService {
    void deleteComment(int commentId, int userId) throws ServiceException;

    void voteComment(int userId, int commentId, int vote) throws ServiceException;
}
