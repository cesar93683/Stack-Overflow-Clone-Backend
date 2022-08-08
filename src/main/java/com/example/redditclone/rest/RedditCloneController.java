package com.example.redditclone.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedditCloneController {

    @GetMapping("/")
    public String sayHello() {
        return "index";
    }

}
