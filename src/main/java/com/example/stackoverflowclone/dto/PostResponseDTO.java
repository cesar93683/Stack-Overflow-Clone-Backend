package com.example.stackoverflowclone.dto;

import com.example.stackoverflowclone.entity.PostResponse;

public class PostResponseDTO {

    private String content;

    private int votes;
    private UserDTO user;
    public PostResponseDTO(PostResponse postResponse) {
        content = postResponse.getContent();
        votes = postResponse.getVotes();
        user = new UserDTO(postResponse.getUser());
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
