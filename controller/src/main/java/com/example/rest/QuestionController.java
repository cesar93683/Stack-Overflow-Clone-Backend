package com.example.rest;

import com.example.exceptions.ServiceException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.data.CreateCommentRequest;
import com.example.rest.payload.data.CreateQuestionRequest;
import com.example.rest.payload.data.UpdateContentRequest;
import com.example.rest.payload.data.VoteRequest;
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
@RequestMapping("/api/questions")
public class QuestionController {

    private static final Logger LOGGER = LogManager.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public ResponseEntity<?> getQuestions(@RequestParam(required = false) String page,
                                          @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionService.getQuestions(page != null ? Integer.parseInt(page) : 0,
                    Boolean.parseBoolean(sortedByVotes)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getQuestionsByUserId(@PathVariable String userId, @RequestParam(required = false) String page,
                                                  @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionService.getQuestionsByUserId(Integer.parseInt(userId),
                    page != null ? Integer.parseInt(page) : 0, Boolean.parseBoolean(sortedByVotes)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/users/answered/{userId}")
    public ResponseEntity<?> getQuestionsAnsweredByUserId(@PathVariable String userId, @RequestParam(required = false) String page,
                                                          @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionService.getQuestionsAnsweredByUserId(Integer.parseInt(userId),
                    page != null ? Integer.parseInt(page) : 0, Boolean.parseBoolean(sortedByVotes)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable String id) {
        try {
            return ResponseEntity.ok(questionService.getQuestion(Integer.parseInt(id), getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody CreateQuestionRequest createQuestionRequest) {
        try {
            return ResponseEntity.ok(questionService.createQuestion(createQuestionRequest.getTitle(),
                    createQuestionRequest.getContent(), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@Valid @RequestBody UpdateContentRequest updateContentRequest, @PathVariable String id) {
        try {
            questionService.updateQuestion(Integer.parseInt(id), updateContentRequest.getContent(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
        try {
            questionService.deleteQuestion(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteQuestion(@Valid @RequestBody VoteRequest voteRequest) {
        try {
            if (!UP_VOTE.equals(voteRequest.getAction()) &&
                    !DOWN_VOTE.equals(voteRequest.getAction()) &&
                    !NEUTRAL.equals(voteRequest.getAction())) {
                throw new ServiceException("Invalid vote action");
            }
            questionService.voteQuestion(getUserId(), Integer.parseInt(voteRequest.getId()), voteRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(questionService.createComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

}
