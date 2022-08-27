package com.example.service;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.exceptions.UserException;

public interface UserService {

    UserDTO getUserById(int id) throws UserException;

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void register(User user);

    int getUserId(String username) throws UserException;
}
