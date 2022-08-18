package com.example.stackoverflowclone.entity;

import javax.persistence.*;

@Entity
@Table(name = "post_response_vote")
public class PostResponseVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "vote", nullable = false)
    private String vote;

    @Column(name = "user_id", nullable = false, updatable = false)
    private int userId;

    @Column(name = "post_response_id", nullable = false, updatable = false)
    private int postResponseId;

    @SuppressWarnings("unused")
    public PostResponseVote() {
    }

    public PostResponseVote(int userId, int postResponseId, String vote) {
        this.userId = userId;
        this.postResponseId = postResponseId;
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostResponseId() {
        return postResponseId;
    }

    public void setPostResponseId(int postResponseId) {
        this.postResponseId = postResponseId;
    }
}
