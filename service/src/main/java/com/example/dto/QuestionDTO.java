package com.example.dto;

import com.example.entity.Comment;
import com.example.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDTO {

    private int id;
    private String title;
    private String content;
    private int votes;
    private int numAnswers;
    private List<CommentDTO> comments;
    private UserDTO user;
    private int currVote;
    private Date createdAt;
    private Date updatedAt;

    public QuestionDTO(Question question, boolean includeComments) {
        id = question.getId();
        title = question.getTitle();
        content = question.getContent();
        votes = question.getVotes();
        numAnswers = question.getNumAnswers();
        if (includeComments) {
            comments = new ArrayList<>();
            for (Comment comment : question.getComments()) {
                comments.add(new CommentDTO(comment));
            }
        }
        user = new UserDTO(question.getUser());
        createdAt = question.getCreatedAt();
        updatedAt = question.getUpdatedAt();
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

    public int getNumAnswers() {
        return numAnswers;
    }

    public void setNumAnswers(int numAnswers) {
        this.numAnswers = numAnswers;
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
