package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.exceptions.UserException;
import com.example.stackoverflowclone.payload.GenericResponse;
import com.example.stackoverflowclone.payload.post.*;
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

import static com.example.stackoverflowclone.utils.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {

    Logger LOGGER = LogManager.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping("/by")
    public ResponseEntity<?> getPosts(@RequestParam(required = false) String page) {
        try {
            return ResponseEntity.ok(postService.getPosts(page != null ? Integer.parseInt(page) : 0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String userId, @RequestParam(required = false) String page) {
        try {
            return ResponseEntity.ok(postService.getPostsByUserId(Integer.parseInt(userId),
                    page != null ? Integer.parseInt(page) : 0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        try {
            return ResponseEntity.ok(postService.getPost(Integer.parseInt(id)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        try {
            int postId = postService.createPost(createPostRequest.getTitle(),
                    createPostRequest.getContent(), getUserId());
            return ResponseEntity.ok(new CreatePostResponse(0, postId));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdatePostRequest updatePostRequest, @PathVariable String id) {
        try {
            postService.updatePost(Integer.parseInt(id), updatePostRequest.getContent(), getUserId());
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

    @PostMapping("/vote")
    public ResponseEntity<?> votePost(@Valid @RequestBody VotePostRequest votePostRequest) {
        try {
            if (!UP_VOTE.equals(votePostRequest.getAction()) &&
                    !DOWN_VOTE.equals(votePostRequest.getAction()) &&
                    !NEUTRAL.equals(votePostRequest.getAction())) {
                throw new PostException("Invalid vote action");
            }
            postService.votePost(getUserId(), Integer.parseInt(votePostRequest.getPostId()), votePostRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/postResponse")
    public ResponseEntity<?> createPostResponse(@Valid @RequestBody CreatePostResponseRequest createPostResponseRequest) {
        try {
            postService.createPostResponse(createPostResponseRequest.getContent(), Integer.parseInt(createPostResponseRequest.getPostId()),
                    getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/postResponse/{id}")
    public ResponseEntity<?> updatePostResponse(@Valid @RequestBody UpdatePostResponseRequest updatePostResponseRequest, @PathVariable String id) {
        try {
            postService.updatePostResponse(Integer.parseInt(id), updatePostResponseRequest.getContent(), getUserId());
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
