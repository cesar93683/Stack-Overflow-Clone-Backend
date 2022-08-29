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
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", updatable = false)
    private Comment comment;

    @SuppressWarnings("unused")
    public Vote() {
    }

    public Vote(User user, Post post, Comment comment, String voteType) {
        this.user = user;
        this.comment = comment;
        this.post = post;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
