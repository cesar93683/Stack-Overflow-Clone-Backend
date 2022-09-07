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
        Answer answer = answerRepository.save(new Answer(content, user, question));
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
        if (prevAcceptedAnswer != null) {
            prevAcceptedAnswer.setAccepted(0);
            answerRepository.save(prevAcceptedAnswer);
        }
        answer.setAccepted(1);
        question.setAnswered(1);
        answerRepository.save(answer);
        questionRepository.save(question);
    }

    @Override
    public void voteAnswer(int userId, int answerId, int newVoteType) throws ServiceException {
        User userVoter = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        Vote vote = voteRepository.findByUserIdAndAnswerId(userVoter.getId(), answerId)
                .orElse(new Vote(userVoter, null, answer, null, NEUTRAL));
        int oldVoteType = vote.getVote();
        if (newVoteType == oldVoteType) {
            return;
        }
        answer.setVotes(answer.getVotes() + getVoteDiff(oldVoteType, newVoteType));
        vote.setVote(newVoteType);
        User userAnswer = answer.getUser();
        if (userVoter != userAnswer) {
            updateReputation(newVoteType, userVoter, oldVoteType, userAnswer);
        }
        answerRepository.save(answer);
        voteRepository.save(vote);
    }

    @Override
    public CommentDTO createComment(String content, int answerId, int userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        Comment comment = commentRepository.save(new Comment(content, user, null, answer));
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

    private void updateReputation(int newVoteType, User userVoter, int oldVoteType, User userAnswer) {
        if (newVoteType == UP_VOTE) {
            if (oldVoteType == NEUTRAL) {
                userAnswer.setReputation(userAnswer.getReputation() + 10);
            } else { // DOWN_VOTE
                userAnswer.setReputation(userAnswer.getReputation() + 12);
                userVoter.setReputation(userVoter.getReputation() + 1);
            }
        } else if (newVoteType == NEUTRAL) {
            if (oldVoteType == UP_VOTE) {
                userAnswer.setReputation(userAnswer.getReputation() - 10);
            } else { // DOWN_VOTE
                userAnswer.setReputation(userAnswer.getReputation() + 2);
                userVoter.setReputation(userVoter.getReputation() + 1);
            }
        } else { // DOWN_VOTE
            userVoter.setReputation(userVoter.getReputation() - 1);
            if (oldVoteType == UP_VOTE) {
                userAnswer.setReputation(userAnswer.getReputation() - 12);
            } else { // NEUTRAL
                userAnswer.setReputation(userAnswer.getReputation() - 2);
            }
        }
    }
}
