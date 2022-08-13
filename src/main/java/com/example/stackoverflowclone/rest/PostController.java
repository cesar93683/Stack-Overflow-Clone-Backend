package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.security.services.UserDetailsImpl;
import com.example.stackoverflowclone.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {

    Logger LOGGER = LogManager.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public List<PostDTO> getPosts() {
        try {
            return postService.getPosts();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable String id) {
        try {
            return postService.getPost(Integer.parseInt(id));
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = getUserId(authentication);
            if (userId == -1) {
                return ResponseEntity.badRequest().body(-1);
            }
            int postId = postService.createPost(userId, createPostRequest);
            return ResponseEntity.ok(postId);
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    private int getUserId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return -1;
    }

}
