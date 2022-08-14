package com.example.stackoverflowclone.dto;

import com.example.stackoverflowclone.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private int id;
    private String username;

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
