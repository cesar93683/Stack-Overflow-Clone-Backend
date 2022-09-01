package com.example.rest;

import com.example.exceptions.QuestionAndAnswerException;
import com.example.exceptions.UserException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.post.*;
import com.example.security.services.UserDetailsImpl;
import com.example.service.QuestionAndAnswerService;
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
    private QuestionAndAnswerService questionAndAnswerService;

    @GetMapping("/all")
    public ResponseEntity<?> getPosts(@RequestParam(required = false) String page,
                                      @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionAndAnswerService.getQuestions(page != null ? Integer.parseInt(page) : 0,
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
            return ResponseEntity.ok(questionAndAnswerService.getQuestionsByUserId(Integer.parseInt(userId),
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
            return ResponseEntity.ok(questionAndAnswerService.getQuestion(Integer.parseInt(id), getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/responses/{postId}")
    public ResponseEntity<?> getPostResponses(@PathVariable String postId, @RequestParam(required = false) String page,
                                              @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionAndAnswerService.getAnswers(Integer.parseInt(postId),
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
            return ResponseEntity.ok(questionAndAnswerService.createQuestion(createPostRequest.getTitle(),
                    createPostRequest.getContent(), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdateRequest updateRequest, @PathVariable String id) {
        try {
            questionAndAnswerService.updateQuestion(Integer.parseInt(id), updateRequest.getContent(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            questionAndAnswerService.deleteQuestion(Integer.parseInt(id), getUserId());
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
                throw new QuestionAndAnswerException("Invalid vote action");
            }
            questionAndAnswerService.voteQuestion(getUserId(), Integer.parseInt(postVoteRequest.getPostId()), postVoteRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(questionAndAnswerService.createQuestionComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getPostId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        try {
            questionAndAnswerService.deleteComment(Integer.parseInt(id), getUserId());
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
                    !NEUTRAL.equals(voteRequest.getAction())) {
                throw new QuestionAndAnswerException("Invalid vote action");
            }
            questionAndAnswerService.voteComment(getUserId(), Integer.parseInt(voteRequest.getId()), voteRequest.getAction());
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
