package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public List<PostDTO> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable String id) {
        try {
            return postService.getPost(Integer.parseInt(id));
        } catch (PostException e) {
            return null;
        }
    }

}
