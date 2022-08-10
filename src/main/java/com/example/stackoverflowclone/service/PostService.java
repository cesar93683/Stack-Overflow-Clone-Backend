package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.entity.Post;
import com.example.stackoverflowclone.exceptions.PostException;

import java.util.List;

public interface PostService {
    List<Post> getPosts();
    Post getPost(int id) throws PostException;
}
