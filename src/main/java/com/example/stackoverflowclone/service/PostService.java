package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.PostException;

import java.util.List;

public interface PostService {
    List<PostDTO> getPosts();
    PostDTO getPost(int id) throws PostException;
}
