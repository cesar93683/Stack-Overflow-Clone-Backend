package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.entity.Post;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPost(int id) throws PostException {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post not found with id: " + id));
    }
}
