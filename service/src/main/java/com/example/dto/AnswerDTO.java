package com.example.dto;

import com.example.entity.Answer;
import com.example.entity.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AnswerDTO {

    private int id;
    private String content;
    private int votes;
    private int accepted;
    private List<CommentDTO> comments;
    private UserDTO user;
    private int currVote;
    private int questionId;
    private Date createdAt;
    private Date updatedAt;

    public AnswerDTO() {
    }

    public AnswerDTO(Answer answer, boolean includeComments) {
        id = answer.getId();
        content = answer.getContent();
        votes = answer.getVotes();
        accepted = answer.getAccepted();
        comments = new ArrayList<>();
        if (includeComments) {
            for (Comment comment : answer.getComments()) {
                comments.add(new CommentDTO(comment));
            }
        }
        user = new UserDTO(answer.getUser());
        currVote = 0;
        questionId = answer.getQuestion().getId();
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

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
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

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerDTO answerDTO = (AnswerDTO) o;
        return id == answerDTO.id && votes == answerDTO.votes && accepted == answerDTO.accepted && currVote == answerDTO.currVote && questionId == answerDTO.questionId && Objects.equals(content, answerDTO.content) && Objects.equals(comments, answerDTO.comments) && Objects.equals(user, answerDTO.user) && Objects.equals(createdAt, answerDTO.createdAt) && Objects.equals(updatedAt, answerDTO.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, votes, accepted, comments, user, currVote, questionId, createdAt, updatedAt);
    }
}
