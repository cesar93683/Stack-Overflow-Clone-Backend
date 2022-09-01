package com.example.rest;

import com.example.exceptions.ServiceException;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.post.VoteRequest;
import com.example.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.rest.utils.Utils.getUserId;
import static com.example.utils.Constants.NEUTRAL;
import static com.example.utils.Constants.UP_VOTE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger LOGGER = LogManager.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        try {
            commentService.deleteComment(Integer.parseInt(id), getUserId());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteComment(@Valid @RequestBody VoteRequest voteRequest) {
        try {
            if (!UP_VOTE.equals(voteRequest.getAction()) &&
                    !NEUTRAL.equals(voteRequest.getAction())) {
                throw new ServiceException("Invalid vote action");
            }
            commentService.voteComment(getUserId(), Integer.parseInt(voteRequest.getId()), voteRequest.getAction());
            return ResponseEntity.ok(new GenericResponse(0));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

}
