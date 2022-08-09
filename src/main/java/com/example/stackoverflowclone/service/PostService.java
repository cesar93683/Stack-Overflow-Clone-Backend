package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.entity.Post;

import java.util.List;

public interface PostService {
    public List<Post> getPosts();
    public Post getPost(String id);
}
