package com.example.dto;

import com.example.entity.User;

public class UserDTO {

    private int id;
    private String username;

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
