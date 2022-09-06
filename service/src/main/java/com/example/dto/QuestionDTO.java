package com.example.dto;

import com.example.entity.Answer;
import com.example.entity.Comment;
import com.example.entity.Question;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionDTO {

    private int id;
    private String title;
    private String content;
    private int votes;
    private int numAnswers;
    private int answered;
    private List<AnswerDTO> answers;
    private List<CommentDTO> comments;
    private UserDTO user;
    private int currVote;
    private Date createdAt;
    private Date updatedAt;

    public QuestionDTO(Question question, boolean includeCommentsAndAnswers) {
        id = question.getId();
        title = question.getTitle();
        content = question.getContent();
        votes = question.getVotes();
        numAnswers = question.getNumAnswers();
        answered = question.getAnswered();
        answers = new ArrayList<>();
        comments = new ArrayList<>();
        if (includeCommentsAndAnswers) {
            for (Answer answer : question.getAnswers()) {
                answers.add(new AnswerDTO(answer, true));
            }
            for (Comment comment : question.getComments()) {
                comments.add(new CommentDTO(comment));
            }
        }
        user = new UserDTO(question.getUser());
        currVote = 0;
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

    public int getAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public int getNumAnswers() {
        return numAnswers;
    }

    public void setNumAnswers(int numAnswers) {
        this.numAnswers = numAnswers;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
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
