package com.example.redditclone.service;

import com.example.redditclone.entity.Post;

import java.util.List;

public interface PostService {
    public List<Post> getPosts();
    public Post getPost(String id);
}
