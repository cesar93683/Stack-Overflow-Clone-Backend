package com.example.rest;

import com.example.exceptions.ServiceException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.data.CreateAnswerRequest;
import com.example.rest.payload.data.CreateCommentRequest;
import com.example.rest.payload.data.UpdateAnswerRequest;
import com.example.rest.payload.data.VoteRequest;
import com.example.service.AnswerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.rest.utils.Utils.getUserId;
import static com.example.utils.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private static final Logger LOGGER = LogManager.getLogger(AnswerController.class);

    @Autowired
    private AnswerService answerService;

    @PostMapping("/")
    public ResponseEntity<?> createAnswer(@Valid @RequestBody CreateAnswerRequest createAnswerRequest) {
        try {
            return ResponseEntity.ok(answerService.createAnswer(createAnswerRequest.getContent(), getUserId(),
                    Integer.parseInt(createAnswerRequest.getQuestionId())));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnswer(@PathVariable String id) {
        try {
            return ResponseEntity.ok(answerService.getAnswer(Integer.parseInt(id)));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswer(@Valid @RequestBody UpdateAnswerRequest updateAnswerRequest, @PathVariable String id) {
        try {
            answerService.updateAnswer(Integer.parseInt(id), updateAnswerRequest.getContent(), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable String id) {
        try {
            answerService.deleteAnswer(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<?> acceptAnswer(@PathVariable String id) {
        try {
            answerService.acceptAnswer(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteAnswer(@Valid @RequestBody VoteRequest voteRequest) {
        try {
            int vote = Integer.parseInt(voteRequest.getVote());
            if (UP_VOTE != vote && NEUTRAL != vote && DOWN_VOTE != vote) {
                throw new ServiceException("Invalid vote");
            }
            answerService.voteAnswer(getUserId(), Integer.parseInt(voteRequest.getId()), vote);
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(answerService.createComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(ERROR_CODE_BAD_REQUEST));
        }
    }

}
