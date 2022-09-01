package com.example.rest;

import com.example.exceptions.ServiceException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.data.CreateAnswerRequest;
import com.example.rest.payload.data.CreateCommentRequest;
import com.example.rest.payload.data.UpdateContentRequest;
import com.example.rest.payload.data.VoteRequest;
import com.example.service.AnswerService;
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
@RequestMapping("/api/answers")
public class AnswerController {

    private static final Logger LOGGER = LogManager.getLogger(AnswerController.class);

    @Autowired
    private AnswerService answerService;

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswersByQuestionId(@PathVariable String questionId) {
        try {
            return ResponseEntity.ok(answerService.getAnswersByQuestionId(Integer.parseInt(questionId),
                    getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createAnswer(@Valid @RequestBody CreateAnswerRequest createAnswerRequest) {
        try {
            return ResponseEntity.ok(answerService.createAnswer(createAnswerRequest.getContent(), getUserId(),
                    Integer.parseInt(createAnswerRequest.getQuestionId())));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswer(@Valid @RequestBody UpdateContentRequest updateContentRequest, @PathVariable String id) {
        try {
            answerService.updateAnswer(Integer.parseInt(id), updateContentRequest.getContent(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable String id) {
        try {
            answerService.deleteAnswer(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteAnswer(@Valid @RequestBody VoteRequest voteRequest) {
        try {
            if (!UP_VOTE.equals(voteRequest.getAction()) &&
                    !DOWN_VOTE.equals(voteRequest.getAction()) &&
                    !NEUTRAL.equals(voteRequest.getAction())) {
                throw new ServiceException("Invalid vote action");
            }
            answerService.voteAnswer(getUserId(), Integer.parseInt(voteRequest.getId()), voteRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(answerService.createComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

}
