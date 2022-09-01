package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.entity.*;
import com.example.exceptions.ServiceException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<AnswerDTO> getAnswers(int answerId, int page, boolean sortedByVotes, int userId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        List<AnswerDTO> answers = answerRepository.findAllByQuestionId(answerId, pageable)
                .stream()
                .map((Answer answer) -> new AnswerDTO(answer, true))
                .collect(Collectors.toList());
        if (userId != NO_USER_ID) {
            updateAnswersWithCurrVote(answers, userId);
            updateCommentsWithCurrVoteFromAnswers(answers, userId);
        }
        return answers;
    }

    @Override
    public AnswerDTO createAnswer(String content, int userId, int questionId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        if (hasRespondedToThisQuestion(userId, questionId)) {
            throw new ServiceException("User with id " + user + " has already answer question with id: " + questionId);
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
    public void voteAnswer(int userId, int answerId, String voteType) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException("Answer not found with id: " + answerId));
        Vote vote = voteRepository.findByUserIdAndAnswerId(user.getId(), answerId)
                .orElse(new Vote(user, null, answer, null, NEUTRAL));
        answer.setVotes(answer.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        answerRepository.save(answer);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
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

    private boolean hasRespondedToThisQuestion(int userId, int questionId) {
        for (Answer answer : answerRepository.findAllByQuestionId(questionId)) {
            if (answer.getUser().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    private void updateAnswersWithCurrVote(List<AnswerDTO> answers, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndAnswerIdIn(userId, getAnswerIds(answers));
        for (Vote vote : votes) {
            answers.stream()
                    .filter(answer -> answer.getId() == vote.getAnswer().getId())
                    .findFirst()
                    .ifPresent(answer -> answer.setCurrVote(vote.getVoteType()));
        }
    }

    private List<Integer> getAnswerIds(List<AnswerDTO> answers) {
        return answers
                .stream()
                .map(AnswerDTO::getId)
                .collect(Collectors.toList());
    }

    private void updateCommentsWithCurrVoteFromAnswers(List<AnswerDTO> answers, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndCommentIdIn(userId, getCommentIdsFromAnswers(answers));
        for (Vote vote : votes) {
            for (AnswerDTO answer : answers) {
                answer.getComments().stream()
                        .filter(commentDTO -> commentDTO.getId() == vote.getComment().getId())
                        .findFirst()
                        .ifPresent(commentDTO -> commentDTO.setCurrVote(vote.getVoteType()));
            }
        }
    }

    private List<Integer> getCommentIdsFromAnswers(List<AnswerDTO> answers) {
        return answers.stream()
                .flatMap(answer -> answer.getComments().stream())
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
    }
}
