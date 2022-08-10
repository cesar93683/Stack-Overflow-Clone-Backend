package com.example.stackoverflowclone.dto;

import com.example.stackoverflowclone.entity.Post;
import com.example.stackoverflowclone.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private int id;
    private String username;
    private List<PostDTO> posts;

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        posts = new ArrayList<>();
        for (Post post : user.getPosts()) {
            PostDTO postDTO = new PostDTO();
            postDTO.setContent(post.getContent());
            postDTO.setId(post.getId());
            posts.add(postDTO);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }

}
