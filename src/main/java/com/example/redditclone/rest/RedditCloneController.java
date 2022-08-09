package com.example.redditclone.rest;

import com.example.redditclone.entity.Post;
import com.example.redditclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RedditCloneController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    public Post getPost(@PathVariable String id) {
        return postService.getPost(id);
    }

}
