package com.example.rest;

import com.example.exceptions.ServiceException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.post.CreateCommentRequest;
import com.example.rest.payload.post.CreatePostRequest;
import com.example.rest.payload.post.PostVoteRequest;
import com.example.rest.payload.post.UpdateRequest;
import com.example.service.QuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.rest.utils.Utils.getUserId;
import static com.example.rest.utils.Utils.getUserIdIfExists;
import static com.example.utils.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class QuestionController {

    private static final Logger LOGGER = LogManager.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @GetMapping("/all")
    public ResponseEntity<?> getPosts(@RequestParam(required = false) String page,
                                      @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionService.getQuestions(page != null ? Integer.parseInt(page) : 0,
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
            return ResponseEntity.ok(questionService.getQuestionsByUserId(Integer.parseInt(userId),
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
            return ResponseEntity.ok(questionService.getQuestion(Integer.parseInt(id), getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        try {
            return ResponseEntity.ok(questionService.createQuestion(createPostRequest.getTitle(),
                    createPostRequest.getContent(), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdateRequest updateRequest, @PathVariable String id) {
        try {
            questionService.updateQuestion(Integer.parseInt(id), updateRequest.getContent(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            questionService.deleteQuestion(Integer.parseInt(id), getUserId());
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
                throw new ServiceException("Invalid vote action");
            }
            questionService.voteQuestion(getUserId(), Integer.parseInt(postVoteRequest.getPostId()), postVoteRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(questionService.createQuestionComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getPostId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

}
