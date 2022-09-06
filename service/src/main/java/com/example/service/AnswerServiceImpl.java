package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.entity.*;
import com.example.exceptions.ServiceException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.example.utils.Constants.*;
import static com.example.utils.Utils.getVoteDiff;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Override
    public AnswerDTO createAnswer(String content, int userId, int questionId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        if (hasRespondedToThisQuestion(userId, questionId)) {
            throw new ServiceException("User with id " + userId + " has already answer question with id: " + questionId);
        }
        question.setNumAnswers(question.getNumAnswers() + 1);
        questionRepository.save(question);

        Answer answer = new Answer(content, user, question);
        int answerId = answerRepository.save(answer).getId();
        answer.setId(answerId);
        voteRepository.save(new Vote(user, null, answer, null, UP_VOTE));
        AnswerDTO answerDTO = new AnswerDTO(answer, false);
        answerDTO.setCurrVote(UP_VOTE);
        return answerDTO;

    }

    @Override
    public void updateAnswer(int answerId, String content, int userId) throws ServiceException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        if (answer.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create answer with id: " + answerId);
        }
        answer.setContent(content);
        answer.setUpdatedAt(new Date());
        answerRepository.save(answer);
    }

    @Override
    public void deleteAnswer(int answerId, int userId) throws ServiceException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        if (answer.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create answer with id: " + answerId);
        }
        Question question = answer.getQuestion();
        question.setNumAnswers(question.getNumAnswers() - 1);
        questionRepository.save(question);
        answerRepository.delete(answer);
    }

    @Override
    public void acceptAnswer(int answerId, int userId) throws ServiceException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        Question question = answer.getQuestion();
        if (question.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create question with id: " + question.getId());
        }
        List<Answer> answers = question.getAnswers();
        Answer prevAcceptedAnswer = answers.stream().
                filter(questionAnswer -> questionAnswer.getAccepted() == ANSWER_ACCEPTED)
                .findFirst()
                .orElse(null);
        if (prevAcceptedAnswer == answer) {
            throw new ServiceException("Answer has already been set as accepted");
        }
        if (prevAcceptedAnswer == null) {
            answer.setAccepted(1);
        }
        question.setAnswered(1);
        answerRepository.save(answer);
        questionRepository.save(question);
    }

    @Override
    public void voteAnswer(int userId, int answerId, int voteType) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        Vote vote = voteRepository.findByUserIdAndAnswerId(user.getId(), answerId)
                .orElse(new Vote(user, null, answer, null, NEUTRAL));
        answer.setVotes(answer.getVotes() + getVoteDiff(vote.getVote(), voteType));
        answerRepository.save(answer);
        vote.setVote(voteType);
        voteRepository.save(vote);
    }

    @Override
    public CommentDTO createComment(String content, int answerId, int userId) throws ServiceException {
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

    private boolean hasRespondedToThisQuestion(int userId, int questionId) {
        return answerRepository.findAllByQuestionId(questionId)
                .stream()
                .anyMatch(answer -> answer.getUser().getId() == userId);
    }
}
