package com.example.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "vote", nullable = false)
    private int vote;

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

    public Vote(User user, Question question, Answer answer, Comment comment, int voteType) {
        this.user = user;
        this.question = question;
        this.answer = answer;
        this.comment = comment;
        this.vote = voteType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote1 = (Vote) o;
        return id == vote1.id && vote == vote1.vote && Objects.equals(user, vote1.user) && Objects.equals(question, vote1.question) && Objects.equals(answer, vote1.answer) && Objects.equals(comment, vote1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vote, user, question, answer, comment);
    }
}
