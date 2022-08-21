package com.example.stackoverflowclone.dto;

import com.example.stackoverflowclone.entity.Comment;
import com.example.stackoverflowclone.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    private int id;
    private String title;
    private String content;
    private int votes;
    private List<CommentDTO> comments;
    private UserDTO user;
    private Date createdAt;
    private Date updatedAt;

    public PostDTO(Post post, boolean includeComments) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        votes = post.getVotes();
        if (includeComments) {
            comments = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                comments.add(new CommentDTO(comment));
            }
        }
        user = new UserDTO(post.getUser());
        createdAt = post.getCreatedAt();
        updatedAt = post.getUpdatedAt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
