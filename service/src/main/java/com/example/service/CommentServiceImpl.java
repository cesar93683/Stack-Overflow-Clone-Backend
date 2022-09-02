package com.example.service;

import com.example.entity.Comment;
import com.example.entity.User;
import com.example.entity.Vote;
import com.example.exceptions.ServiceException;
import com.example.repository.CommentRepository;
import com.example.repository.UserRepository;
import com.example.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.utils.Constants.NEUTRAL;
import static com.example.utils.Utils.getVoteDiff;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private CommentRepository commentRepository;

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
    public void voteComment(int userId, int commentId, int voteType) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("Comment not found with id: " + commentId));
        Vote vote = voteRepository.findByUserIdAndCommentId(user.getId(), commentId)
                .orElse(new Vote(user, null, null, comment, NEUTRAL));

        comment.setVotes(comment.getVotes() + getVoteDiff(vote.getVote(), voteType));
        commentRepository.save(comment);
        vote.setVote(voteType);
        voteRepository.save(vote);
    }
}
