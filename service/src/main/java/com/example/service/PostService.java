package com.example.service;

import com.example.dto.CommentDTO;
import com.example.dto.PostDTO;
import com.example.entity.Post;
import com.example.exceptions.PostException;

import java.util.List;

public interface PostService {
    List<PostDTO> getPosts(int page, boolean sortByVotes, int userId);

    List<PostDTO> getPostsByUserId(int userId, int page, boolean sortedByVotes, int userIdIfExists);

    PostDTO getPost(int postId, int userId) throws PostException;

    List<PostDTO> getPostResponses(int postId, int page, boolean sortedByVotes, int userId);

    PostDTO createPost(String title, String content, int postResponseId, int userId) throws PostException;

    void updatePost(int postId, String content, int userId) throws PostException;

    void deletePost(int postId, int userId) throws PostException;

    void votePost(int userId, int postId, String vote) throws PostException;

    CommentDTO createComment(String content, int postId, int userId) throws PostException;

    void deleteComment(int commentId, int userId) throws PostException;

    void voteComment(int userId, int commentId, String vote) throws PostException;
}
