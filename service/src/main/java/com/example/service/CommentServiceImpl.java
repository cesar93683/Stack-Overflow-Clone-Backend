package com.example.service;

import com.example.dto.CommentDTO;
import com.example.entity.*;
import com.example.exceptions.ServiceException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.utils.Constants.NEUTRAL;
import static com.example.utils.Constants.UP_VOTE;
import static com.example.utils.Utils.getVoteDiff;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDTO createQuestionComment(String content, int questionId, int userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        Comment comment = new Comment(content, user, question, null);
        int commentId = commentRepository.save(comment).getId();
        comment.setId(commentId);
        voteRepository.save(new Vote(user, null, null, comment, UP_VOTE));
        CommentDTO commentDTO = new CommentDTO(comment);
        commentDTO.setCurrVote(UP_VOTE);
        return commentDTO;
    }

    @Override
    public CommentDTO createAnswerComment(String content, int answerId, int userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        Comment comment = new Comment(content, user, null, answer);
        int commentId = commentRepository.save(comment).getId();
        comment.setId(commentId);
        voteRepository.save(new Vote(user, null, null, comment, UP_VOTE));
        CommentDTO commentDTO = new CommentDTO(comment);
        commentDTO.setCurrVote(UP_VOTE);
        return commentDTO;
    }

    @Override
    public void deleteComment(int commentId, int userId) throws ServiceException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("Comment not found with id: " + commentId));
        if (comment.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create comment with id: " + commentId);
        }
        commentRepository.delete(comment);
    }

    @Override
    public void voteComment(int userId, int commentId, String voteType) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("Comment not found with id: " + commentId));
        Vote vote = voteRepository.findByUserIdAndCommentId(user.getId(), commentId)
                .orElse(new Vote(user, null, null, comment, NEUTRAL));

        comment.setVotes(comment.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        commentRepository.save(comment);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
    }
}
