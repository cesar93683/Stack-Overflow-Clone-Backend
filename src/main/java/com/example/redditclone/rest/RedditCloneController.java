package com.example.redditclone.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedditCloneController {

    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }

}
