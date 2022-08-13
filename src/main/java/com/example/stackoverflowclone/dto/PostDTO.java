package com.example.stackoverflowclone.dto;

import com.example.stackoverflowclone.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    private int id;
    private String content;
    private String title;
    private UserDTO user;

    public PostDTO() {
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.title = post.getTitle();
        user = new UserDTO();
        user.setUsername(post.getUser().getUsername());
        user.setId(post.getUser().getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

}
