package com.example.stackoverflowclone.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "post_vote")
public class PostVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "vote", nullable = false)
    @Size(max = 9)
    private String vote;

    @Column(name = "user_id", nullable = false, updatable = false)
    private int userId;

    @Column(name = "post_id", nullable = false, updatable = false)
    private int postId;

    @SuppressWarnings("unused")
    public PostVote() {
    }

    public PostVote(int userId, int postId, String vote) {
        this.userId = userId;
        this.postId = postId;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
