package com.example.rest;

import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.post.CreateCommentRequest;
import com.example.service.AnswerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.rest.utils.Utils.getUserId;
import static com.example.rest.utils.Utils.getUserIdIfExists;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private static final Logger LOGGER = LogManager.getLogger(AnswerController.class);

    @Autowired
    private AnswerService answerService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnswers(@PathVariable String id, @RequestParam(required = false) String page,
                                        @RequestParam(required = false) String sortedByVotes) {
        try {
            return ResponseEntity.ok(answerService.getAnswers(Integer.parseInt(id),
                    page != null ? Integer.parseInt(page) : 0,
                    Boolean.parseBoolean(sortedByVotes),
                    getUserIdIfExists()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        try {
            return ResponseEntity.ok(answerService.createAnswerComment(createCommentRequest.getContent(),
                    Integer.parseInt(createCommentRequest.getPostId()), getUserId()));
        } catch (Exception e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest().body(new GenericResponse(1));
        }
    }

}
