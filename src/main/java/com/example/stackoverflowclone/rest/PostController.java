package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.payload.auth.response.AuthResponse;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.security.services.UserDetailsImpl;
import com.example.stackoverflowclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public List<PostDTO> getPosts() {
        try {
            return postService.getPosts();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable String id) {
        try {
            return postService.getPost(Integer.parseInt(id));
        } catch (Exception e) {
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
            return ResponseEntity.badRequest().body(-1);
        }
    }

    private int getUserId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return -1;
    }

}
