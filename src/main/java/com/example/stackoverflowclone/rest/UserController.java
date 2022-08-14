package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.dto.UserDTO;
import com.example.stackoverflowclone.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable String id) {
        try {
            return userService.getUserById(Integer.parseInt(id));
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

}
