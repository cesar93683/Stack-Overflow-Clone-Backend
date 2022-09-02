package com.example.dto;

import com.example.entity.Answer;
import com.example.entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDTO {

    private int id;
    private String content;
    private int votes;
    private List<CommentDTO> comments;
    private UserDTO user;
    private int currVote;
    private Date createdAt;
    private Date updatedAt;

    public AnswerDTO(Answer answer, boolean includeComments) {
        id = answer.getId();
        content = answer.getContent();
        votes = answer.getVotes();
        if (includeComments) {
            comments = new ArrayList<>();
            for (Comment comment : answer.getComments()) {
                comments.add(new CommentDTO(comment));
            }
        }
        user = new UserDTO(answer.getUser());
        createdAt = answer.getCreatedAt();
        updatedAt = answer.getUpdatedAt();
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
