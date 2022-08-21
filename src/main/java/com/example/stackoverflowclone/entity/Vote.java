package com.example.stackoverflowclone.entity;

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

    @Column(name = "user_id", nullable = false, updatable = false)
    private int userId;

    @Column(name = "post_id", nullable = false, updatable = false)
    private int postId;

    @Column(name = "comment_id", nullable = false, updatable = false)
    private int commentId;

    @SuppressWarnings("unused")
    public Vote() {
    }

    public Vote(int userId, int postId, int commentId, String voteType) {
        this.userId = userId;
        this.commentId = commentId;
        this.postId = postId;
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

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
