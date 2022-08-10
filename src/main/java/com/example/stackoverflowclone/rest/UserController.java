package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.entity.User;
import com.example.stackoverflowclone.exceptions.UserException;
import com.example.stackoverflowclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        try {
            return userService.getUserById(Integer.parseInt(id));
        } catch (UserException e) {
            return null;
        }
    }

}
