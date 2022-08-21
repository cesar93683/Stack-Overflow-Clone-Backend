package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.PostException;

import java.util.List;

public interface PostService {
    List<PostDTO> getPosts(int page, boolean sortByVotes);

    List<PostDTO> getPostsByUserId(int userId, int page);

    PostDTO getPost(int id) throws PostException;

    List<PostDTO> getPostResponses(int postId, int page, boolean sortedByVotes);

    int createPost(String title, String content, int postResponseId, int userId) throws PostException;

    void updatePost(int postId, String content, int userId) throws PostException;

    void deletePost(int postId, int userId) throws PostException;

    void votePost(int userId, int postId, String vote) throws PostException;

    int createComment(String content, int postId, int userId) throws PostException;

    void updateComment(int commentId, String content, int userId) throws PostException;

    void deleteComment(int commentId, int userId) throws PostException;

    void voteComment(int userId, int commentId, String vote) throws PostException;
}
