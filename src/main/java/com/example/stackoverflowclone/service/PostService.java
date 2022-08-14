package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.payload.post.UpdatePostRequest;

import java.util.List;

public interface PostService {
    List<PostDTO> getPosts(int page);
    PostDTO getPost(int id) throws PostException;
    int createPost(CreatePostRequest createPostRequest, int userId) throws PostException;
    void updatePost(int postId, UpdatePostRequest updatePostRequest, int userId) throws PostException;
    void deletePost(int postId, int userId) throws PostException;
}
