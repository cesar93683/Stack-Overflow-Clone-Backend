package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.dto.QuestionDTO;
import com.example.entity.*;
import com.example.exceptions.QuestionAndAnswerException;
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

@Service
public class QuestionAndAnswerServiceImpl implements QuestionAndAnswerService {

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
    public List<QuestionDTO> getQuestions(int page, boolean sortedByVotes, int userId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        List<QuestionDTO> questions = questionRepository.findAll(pageable)
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
        if (userId != NO_USER_ID) {
            updateQuestionsWithCurrVote(questions, userId);
        }
        return questions;
    }

    @Override
    public List<QuestionDTO> getQuestionsByUserId(int userId, int page, boolean sortedByVotes, int userIdIfExists) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        List<QuestionDTO> questions = questionRepository.findAllByUserId(userId, pageable)
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
        if (userId != NO_USER_ID) {
            updateQuestionsWithCurrVote(questions, userId);
        }
        return questions;
    }

    @Override
    public QuestionDTO getQuestion(int questionId, int userId) throws QuestionAndAnswerException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionAndAnswerException("Question not found with id: " + questionId));
        QuestionDTO questionDTO = new QuestionDTO(question, true);
        if (userId != NO_USER_ID) {
            voteRepository.findByUserIdAndQuestionId(userId, questionId)
                    .ifPresent(value -> questionDTO.setCurrVote(value.getVoteType()));
            updateCommentsWithCurrVote(questionDTO.getComments(), userId);
        }
        return questionDTO;
    }

    @Override
    public QuestionDTO createQuestion(String title, String content, int userId) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Question question = new Question(title, content, user);
        int questionId = questionRepository.save(question).getId();
        question.setId(questionId);
        voteRepository.save(new Vote(user, question, null, null, UP_VOTE));
        QuestionDTO questionDTO = new QuestionDTO(question, false);
        questionDTO.setCurrVote(UP_VOTE);
        return questionDTO;
    }

    @Override
    public void updateQuestion(int questionId, String content, int userId) throws QuestionAndAnswerException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionAndAnswerException("Question not found with id: " + questionId));
        if (question.getUser().getId() != userId) {
            throw new QuestionAndAnswerException("User with id: " + userId + " did not create question with id: " + questionId);
        }
        question.setContent(content);
        question.setUpdatedAt(new Date());
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(int questionId, int userId) throws QuestionAndAnswerException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionAndAnswerException("Question not found with id: " + questionId));
        if (question.getUser().getId() != userId) {
            throw new QuestionAndAnswerException("User with id: " + userId + " did not create question with id: " + questionId);
        }
        questionRepository.delete(question);
    }

    @Override
    public void voteQuestion(int userId, int questionId, String voteType) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionAndAnswerException("Question not found with id: " + questionId));
        Vote vote = voteRepository.findByUserIdAndQuestionId(user.getId(), questionId)
                .orElse(new Vote(user, question, null, null, NEUTRAL));

        question.setVotes(question.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        questionRepository.save(question);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
    }

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
    public AnswerDTO createAnswer(String content, int userId, int questionId) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionAndAnswerException("Question not found with id: " + questionId));
        if (hasRespondedToThisQuestion(userId, questionId)) {
            throw new QuestionAndAnswerException("User with id " + user + " has already answer question with id: " + questionId);
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
    public void updateAnswer(int answerId, String content, int userId) throws QuestionAndAnswerException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionAndAnswerException("Answer not found with id: " + answerId));
        if (answer.getUser().getId() != userId) {
            throw new QuestionAndAnswerException("User with id: " + userId + " did not create answer with id: " + answerId);
        }
        answer.setContent(content);
        answer.setUpdatedAt(new Date());
        answerRepository.save(answer);
    }

    @Override
    public void deleteAnswer(int answerId, int userId) throws QuestionAndAnswerException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionAndAnswerException("Answer not found with id: " + answerId));
        if (answer.getUser().getId() != userId) {
            throw new QuestionAndAnswerException("User with id: " + userId + " did not create answer with id: " + answerId);
        }
        Question question = answer.getQuestion();
        question.setNumAnswers(question.getNumAnswers() - 1);
        questionRepository.save(question);
        answerRepository.delete(answer);
    }

    @Override
    public void voteAnswer(int userId, int answerId, String voteType) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionAndAnswerException("Answer not found with id: " + answerId));
        Vote vote = voteRepository.findByUserIdAndAnswerId(user.getId(), answerId)
                .orElse(new Vote(user, null, answer, null, NEUTRAL));
        answer.setVotes(answer.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        answerRepository.save(answer);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
    }

    @Override
    public CommentDTO createQuestionComment(String content, int questionId, int userId) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionAndAnswerException("Question not found with id: " + questionId));
        Comment comment = new Comment(content, user, question, null);
        int commentId = commentRepository.save(comment).getId();
        comment.setId(commentId);
        voteRepository.save(new Vote(user, null, null, comment, UP_VOTE));
        CommentDTO commentDTO = new CommentDTO(comment);
        commentDTO.setCurrVote(UP_VOTE);
        return commentDTO;
    }

    @Override
    public CommentDTO createAnswerComment(String content, int answerId, int userId) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new QuestionAndAnswerException("Answer not found with id: " + answerId));
        Comment comment = new Comment(content, user, null, answer);
        int commentId = commentRepository.save(comment).getId();
        comment.setId(commentId);
        voteRepository.save(new Vote(user, null, null, comment, UP_VOTE));
        CommentDTO commentDTO = new CommentDTO(comment);
        commentDTO.setCurrVote(UP_VOTE);
        return commentDTO;
    }

    @Override
    public void deleteComment(int commentId, int userId) throws QuestionAndAnswerException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new QuestionAndAnswerException("Comment not found with id: " + commentId));
        if (comment.getUser().getId() != userId) {
            throw new QuestionAndAnswerException("User with id: " + userId + " did not create comment with id: " + commentId);
        }
        commentRepository.delete(comment);
    }

    @Override
    public void voteComment(int userId, int commentId, String voteType) throws QuestionAndAnswerException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionAndAnswerException("User not found with id: " + userId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new QuestionAndAnswerException("Comment not found with id: " + commentId));
        Vote vote = voteRepository.findByUserIdAndCommentId(user.getId(), commentId)
                .orElse(new Vote(user, null, null, comment, NEUTRAL));

        comment.setVotes(comment.getVotes() + getVoteDiff(vote.getVoteType(), voteType));
        commentRepository.save(comment);
        vote.setVoteType(voteType);
        voteRepository.save(vote);
    }

    private boolean hasRespondedToThisQuestion(int userId, int questionId) {
        for (Answer answer : answerRepository.findAllByQuestionId(questionId)) {
            if (answer.getUser().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    private void updateQuestionsWithCurrVote(List<QuestionDTO> questions, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndQuestionIdIn(userId, getQuestionIds(questions));
        for (Vote vote : votes) {
            questions.stream()
                    .filter(question -> question.getId() == vote.getQuestion().getId())
                    .findFirst()
                    .ifPresent(question -> question.setCurrVote(vote.getVoteType()));
        }
    }

    private List<Integer> getQuestionIds(List<QuestionDTO> questions) {
        return questions
                .stream()
                .map(QuestionDTO::getId)
                .collect(Collectors.toList());
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

    private void updateCommentsWithCurrVote(List<CommentDTO> comments, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndCommentIdIn(userId, getCommentIds(comments));
        for (Vote vote : votes) {
            comments.stream()
                    .filter(comment -> comment.getId() == vote.getComment().getId())
                    .findFirst()
                    .ifPresent(comment -> comment.setCurrVote(vote.getVoteType()));
        }
    }

    private List<Integer> getCommentIds(List<CommentDTO> comments) {
        return comments
                .stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
    }

    private void updateCommentsWithCurrVoteFromQuestions(List<QuestionDTO> questions, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndCommentIdIn(userId, getCommentIdsFromQuestions(questions));
        for (Vote vote : votes) {
            for (QuestionDTO questionDTO : questions) {
                questionDTO.getComments().stream()
                        .filter(commentDTO -> commentDTO.getId() == vote.getComment().getId())
                        .findFirst()
                        .ifPresent(commentDTO -> commentDTO.setCurrVote(vote.getVoteType()));
            }
        }
    }

    private List<Integer> getCommentIdsFromQuestions(List<QuestionDTO> questions) {
        return questions.stream()
                .flatMap(question -> question.getComments().stream())
                .map(CommentDTO::getId)
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

    private int getVoteDiff(String oldVoteType, String newVoteType) {
        if (oldVoteType.equals(newVoteType)) {
            return 0;
        }
        if (newVoteType.equals(DOWN_VOTE)) {
            if (oldVoteType.equals(NEUTRAL)) {
                return -1;
            } else { // UP_VOTE
                return -2;
            }
        } else if (newVoteType.equals(NEUTRAL)) {
            if (oldVoteType.equals(DOWN_VOTE)) {
                return 1;
            } else { // UP_VOTE
                return -1;
            }
        } else { // UP_VOTE
            if (oldVoteType.equals(DOWN_VOTE)) {
                return 2;
            } else { // NEUTRAL
                return 1;
            }
        }
    }
}
