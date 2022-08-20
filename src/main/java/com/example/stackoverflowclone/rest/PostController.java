package com.example.stackoverflowclone.rest;

import com.example.stackoverflowclone.exceptions.PostException;
import com.example.stackoverflowclone.exceptions.UserException;
import com.example.stackoverflowclone.payload.GenericResponse;
import com.example.stackoverflowclone.payload.post.CreatePostRequest;
import com.example.stackoverflowclone.payload.post.CreatePostResponse;
import com.example.stackoverflowclone.payload.post.PostVoteRequest;
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
            String title = createPostRequest.getTitle();
            String content = createPostRequest.getContent();
            String postResponseId = createPostRequest.getPostResponseId();
            if (!((title != null && content != null && postResponseId == null) ||
                    ((title == null && content != null && postResponseId != null)))) {
                throw new PostException("Invalid content passed in");
            }
            int postId = postService.createPost(title, content,
                    postResponseId != null ? Integer.parseInt(postResponseId) : -1, getUserId());
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
    public ResponseEntity<?> votePost(@Valid @RequestBody PostVoteRequest postVoteRequest) {
        try {
            if (!UP_VOTE.equals(postVoteRequest.getAction()) &&
                    !DOWN_VOTE.equals(postVoteRequest.getAction()) &&
                    !NEUTRAL.equals(postVoteRequest.getAction())) {
                throw new PostException("Invalid vote action");
            }
            postService.votePost(getUserId(), Integer.parseInt(postVoteRequest.getPostId()), postVoteRequest.getAction());
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
