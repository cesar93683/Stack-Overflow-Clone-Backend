package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.dto.UserDTO;
import com.example.stackoverflowclone.exceptions.UserException;

public interface UserService {

    UserDTO getUserById(int id) throws UserException;

}
