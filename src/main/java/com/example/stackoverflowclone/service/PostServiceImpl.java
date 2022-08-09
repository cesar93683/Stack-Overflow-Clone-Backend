package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.entity.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Override
    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("this is post 1"));
        posts.add(new Post("this is post 2"));
        return posts;
    }

    @Override
    public Post getPost(String id) {
        return new Post("this is post " + id);
    }
}
