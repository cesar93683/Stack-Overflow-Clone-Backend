package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.payload.post.UpdatePostRequest;

import java.util.List;

public interface PostService {
    List<PostDTO> getPosts();
    PostDTO getPost(int id) throws PostException;
    int createPost(int userId, CreatePostRequest createPostRequest) throws PostException;
    void updatePost(int postId, UpdatePostRequest updatePostRequest, int userId) throws PostException;
}
