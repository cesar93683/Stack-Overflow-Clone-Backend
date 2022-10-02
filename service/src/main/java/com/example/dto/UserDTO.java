package com.example.dto;

import com.example.entity.User;

import java.util.Objects;

public class UserDTO {

    private int id;
    private String username;
    private int reputation;

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        reputation = user.getReputation();
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

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id && reputation == userDTO.reputation && Objects.equals(username, userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, reputation);
    }
}
