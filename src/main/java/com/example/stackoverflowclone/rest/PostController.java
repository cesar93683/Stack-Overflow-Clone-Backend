package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.dto.PostDTO;
import com.example.stackoverflowclone.exceptions.UserException;
import com.example.stackoverflowclone.payload.GenericResponse;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.payload.post.CreatePostResponse;
import com.example.stackoverflowclone.payload.post.UpdatePostRequest;
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

    @GetMapping("/by")
    public List<PostDTO> getPosts(@RequestParam(required = false) String page) {
        try {
            return postService.getPosts(page != null ? Integer.parseInt(page) : 0);
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @GetMapping("/users/{userId}")
    public List<PostDTO> getPostsByUserId(@PathVariable String userId, @RequestParam(required = false) String page) {
        try {
            return postService.getPostsByUserId(Integer.parseInt(userId), page != null ? Integer.parseInt(page) : 0);
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
            int postId = postService.createPost(createPostRequest, getUserId());
            return ResponseEntity.ok(new CreatePostResponse(0, postId));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdatePostRequest updatePostRequest, @PathVariable String id) {
        try {
            postService.updatePost(Integer.parseInt(id), updatePostRequest, getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            postService.deletePost(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    private int getUserId() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        throw new UserException("User id not found");
    }

}
