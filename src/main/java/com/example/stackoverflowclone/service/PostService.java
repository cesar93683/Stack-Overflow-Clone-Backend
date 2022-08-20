package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.PostException;

import java.util.List;

public interface PostService {
    List<PostDTO> getPosts(int page);

    List<PostDTO> getPostsByUserId(int userId, int page);

    PostDTO getPost(int id) throws PostException;

    int createPost(String title, String content, int postResponseId, int userId) throws PostException;

    void updatePost(int postId, String content, int userId) throws PostException;

    void deletePost(int postId, int userId) throws PostException;

    void votePost(int userId, int postId, String vote) throws PostException;
}
