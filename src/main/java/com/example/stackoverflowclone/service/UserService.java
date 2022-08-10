package com.example.stackoverflowclone.service;

import com.example.stackoverflowclone.entity.User;
import com.example.stackoverflowclone.exceptions.UserException;

public interface UserService {

    User getUserById(int id) throws UserException;

}
