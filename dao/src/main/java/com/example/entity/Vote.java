package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "vote", nullable = false)
    private String voteType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", updatable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id", updatable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "comment_id", updatable = false)
    private Comment comment;

    @SuppressWarnings("unused")
    public Vote() {
    }

    public Vote(User user, Question question, Answer answer, Comment comment, String voteType) {
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.comment = comment;
        this.voteType = voteType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
