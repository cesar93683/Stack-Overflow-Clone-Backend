package com.example.dto;

import com.example.entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    private int id;
    private String content;
    private int votes;
    private UserDTO user;
    private String currVote;
    private Date createdAt;
    private Date updatedAt;

    public CommentDTO(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        votes = comment.getVotes();
        user = new UserDTO(comment.getUser());
        createdAt = comment.getCreatedAt();
        updatedAt = comment.getUpdatedAt();
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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getCurrVote() {
        return currVote;
    }

    public void setCurrVote(String currVote) {
        this.currVote = currVote;
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
