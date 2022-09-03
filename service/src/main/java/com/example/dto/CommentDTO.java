package com.example.dto;

import com.example.entity.Comment;

import java.util.Date;

public class CommentDTO {

    private int id;
    private String content;
    private int votes;
    private UserDTO user;
    private int currVote;
    private Date createdAt;

    public CommentDTO(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        votes = comment.getVotes();
        user = new UserDTO(comment.getUser());
        currVote = 0;
        createdAt = comment.getCreatedAt();
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

    public int getCurrVote() {
        return currVote;
    }

    public void setCurrVote(int currVote) {
        this.currVote = currVote;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
