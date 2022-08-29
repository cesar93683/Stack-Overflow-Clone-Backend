package com.example.rest;

import com.example.exceptions.PostException;
import com.example.exceptions.UserException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.post.*;
import com.example.security.services.UserDetailsImpl;
import com.example.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.utils.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger LOGGER = LogManager.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping("/all")
    public ResponseEntity<?> getPosts(@RequestParam(required = false) String page,
                                      @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(postService.getPosts(page != null ? Integer.parseInt(page) : 0,
                    Boolean.parseBoolean(sortedByVotes), getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String userId, @RequestParam(required = false) String page,
                                              @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(postService.getPostsByUserId(Integer.parseInt(userId),
                    page != null ? Integer.parseInt(page) : 0, Boolean.parseBoolean(sortedByVotes),
                    getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        try {
            return ResponseEntity.ok(postService.getPost(Integer.parseInt(id), getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/responses/{postId}")
    public ResponseEntity<?> getPostResponses(@PathVariable String postId, @RequestParam(required = false) String page,
                                              @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(postService.getPostResponses(Integer.parseInt(postId),
                    page != null ? Integer.parseInt(page) : 0,
                    Boolean.parseBoolean(sortedByVotes),
                    getUserIdIfExists()));
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
                throw new PostException("Invalid request");
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
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdateRequest updateRequest, @PathVariable String id) {
        try {
            postService.updatePost(Integer.parseInt(id), updateRequest.getContent(), getUserId());
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

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            int commentId = postService.createComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getPostId()), getUserId());
            return ResponseEntity.ok(new CreateCommentResponse(0, commentId));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComment(@Valid @RequestBody UpdateRequest updateRequest, @PathVariable String id) {
        try {
            postService.updateComment(Integer.parseInt(id), updateRequest.getContent(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        try {
            postService.deleteComment(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/comments/vote")
    public ResponseEntity<?> voteComment(@Valid @RequestBody VoteRequest voteRequest) {
        try {
            if (!UP_VOTE.equals(voteRequest.getAction()) &&
                    !DOWN_VOTE.equals(voteRequest.getAction()) &&
                    !NEUTRAL.equals(voteRequest.getAction())) {
                throw new PostException("Invalid vote action");
            }
            postService.voteComment(getUserId(), Integer.parseInt(voteRequest.getId()), voteRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    private int getUserIdIfExists() {
        try {
            return getUserId();
        } catch (UserException ignored) {
            return NO_USER_ID;
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
