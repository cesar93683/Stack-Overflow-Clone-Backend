package com.example.rest;

import com.example.dto.QuestionsDTO;
import com.example.dto.UserDTO;
import com.example.exceptions.ServiceException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.data.*;
import com.example.service.QuestionService;
import com.example.service.UserService;
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
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getQuestions(@RequestParam(required = false) String page,
                                          @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionService.getQuestions(page != null ? Integer.parseInt(page) : 0,
                    Boolean.parseBoolean(sortedByVotes)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getQuestionsByUserId(@PathVariable String userId, @RequestParam(required = false) String page,
                                                  @RequestParam(required = false) String sortedByVotes) {
        try {
            QuestionsDTO questions = questionService.getQuestionsByUserId(Integer.parseInt(userId),
                    page != null ? Integer.parseInt(page) : 0, Boolean.parseBoolean(sortedByVotes));
            if (questions.getQuestions().size() == 0) {
                UserDTO user = userService.getUserById(Integer.parseInt(userId));
                return ResponseEntity.ok(new GetQuestionsByUserIdResponse(0, user, questions));
            } else {
                return ResponseEntity.ok(new GetQuestionsByUserIdResponse(0, questions.getQuestions().get(0).getUser(),
                        questions));
            }
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @GetMapping("/users/answered/{userId}")
    public ResponseEntity<?> getQuestionsAnsweredByUserId(@PathVariable String userId, @RequestParam(required = false) String page,
                                                          @RequestParam(required = false) String sortedByVotes) {
        try {
            QuestionsDTO questions = questionService.getQuestionsAnsweredByUserId(Integer.parseInt(userId),
                    page != null ? Integer.parseInt(page) : 0, Boolean.parseBoolean(sortedByVotes));
            UserDTO user = userService.getUserById(Integer.parseInt(userId));
            return ResponseEntity.ok(new GetQuestionsByUserIdResponse(0, user, questions));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable String id) {
        try {
            return ResponseEntity.ok(questionService.getQuestion(Integer.parseInt(id), getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody CreateQuestionRequest createQuestionRequest) {
        try {
            return ResponseEntity.ok(questionService.createQuestion(createQuestionRequest.getTitle(),
                    createQuestionRequest.getContent(), createQuestionRequest.getTags(), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@Valid @RequestBody UpdateQuestionRequest updateQuestionRequest, @PathVariable String id) {
        try {
            questionService.updateQuestion(Integer.parseInt(id), updateQuestionRequest.getContent(),
                    updateQuestionRequest.getTags(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
        try {
            questionService.deleteQuestion(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteQuestion(@Valid @RequestBody VoteRequest voteRequest) {
        try {
            int vote = Integer.parseInt(voteRequest.getVote());
            if (UP_VOTE != vote && NEUTRAL != vote && DOWN_VOTE != vote) {
                throw new ServiceException("Invalid vote");
            }
            questionService.voteQuestion(getUserId(), Integer.parseInt(voteRequest.getId()), vote);
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(questionService.createComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getTags() {
        try {
            return ResponseEntity.ok(questionService.getTags());
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @GetMapping("/tagged/{tag}")
    public ResponseEntity<?> getQuestionByTag(@PathVariable String tag, @RequestParam(required = false) String page,
                                              @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(questionService.getQuestionByTag(tag, page != null ? Integer.parseInt(page) : 0,
                    Boolean.parseBoolean(sortedByVotes)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

}
