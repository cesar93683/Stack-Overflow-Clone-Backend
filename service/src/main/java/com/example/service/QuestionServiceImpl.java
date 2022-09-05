package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.dto.QuestionDTO;
import com.example.entity.*;
import com.example.exceptions.ServiceException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.utils.Constants.*;
import static com.example.utils.Utils.getVoteDiff;

@Service
public class QuestionServiceImpl implements QuestionService {

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
    public List<QuestionDTO> getQuestions(int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        return questionRepository.findAll(pageable)
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> getQuestionsByUserId(int userId, int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        return questionRepository.findAllByUserId(userId, pageable)
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> getQuestionsAnsweredByUserId(int userId, int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        return answerRepository.findAllByUserId(userId, pageable)
                .stream()
                .map((Answer answer) -> new QuestionDTO(answer.getQuestion(), false))
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDTO getQuestion(int questionId, int userId) throws ServiceException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        QuestionDTO questionDTO = new QuestionDTO(question, true);
        if (userId != NO_USER_ID) {
            updatedQuestionWithCurrVote(questionDTO, userId);
            updateCommentsWithCurrVote(getComments(questionDTO), userId);
            updateAnswersWithCurrVote(questionDTO.getAnswers(), userId);
        }
        return questionDTO;
    }

    @Override
    public QuestionDTO createQuestion(String title, String content, int userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = new Question(title, content, user);
        int questionId = questionRepository.save(question).getId();
        question.setId(questionId);
        voteRepository.save(new Vote(user, question, null, null, UP_VOTE));
        QuestionDTO questionDTO = new QuestionDTO(question, false);
        questionDTO.setCurrVote(UP_VOTE);
        return questionDTO;
    }

    @Override
    public void updateQuestion(int questionId, String content, int userId) throws ServiceException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        if (question.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create question with id: " + questionId);
        }
        question.setContent(content);
        question.setUpdatedAt(new Date());
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(int questionId, int userId) throws ServiceException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        if (question.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create question with id: " + questionId);
        }
        questionRepository.delete(question);
    }

    @Override
    public void voteQuestion(int userId, int questionId, int voteType) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        Vote vote = voteRepository.findByUserIdAndQuestionId(user.getId(), questionId)
                .orElse(new Vote(user, question, null, null, NEUTRAL));

        question.setVotes(question.getVotes() + getVoteDiff(vote.getVote(), voteType));
        questionRepository.save(question);
        vote.setVote(voteType);
        voteRepository.save(vote);
    }

    @Override
    public CommentDTO createComment(String content, int questionId, int userId) throws ServiceException {
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

    private void updatedQuestionWithCurrVote(QuestionDTO questionDTO, int userId) {
        voteRepository.findByUserIdAndQuestionId(userId, questionDTO.getId())
                .ifPresent(value -> questionDTO.setCurrVote(value.getVote()));
    }

    private List<CommentDTO> getComments(QuestionDTO question) {
        List<CommentDTO> comments = new ArrayList<>(question.getComments());
        for (AnswerDTO answer : question.getAnswers()) {
            comments.addAll(answer.getComments());
        }
        return comments;
    }

    private void updateCommentsWithCurrVote(List<CommentDTO> comments, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndCommentIdIn(userId, getCommentIds(comments));
        for (Vote vote : votes) {
            comments.stream()
                    .filter(comment -> comment.getId() == vote.getComment().getId())
                    .findFirst()
                    .ifPresent(comment -> comment.setCurrVote(vote.getVote()));
        }
    }

    private List<Integer> getCommentIds(List<CommentDTO> comments) {
        return comments
                .stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
    }

    private void updateAnswersWithCurrVote(List<AnswerDTO> answers, int userId) {
        List<Vote> votes = voteRepository.findByUserIdAndAnswerIdIn(userId, getAnswerIds(answers));
        for (Vote vote : votes) {
            answers.stream()
                    .filter(answer -> answer.getId() == vote.getAnswer().getId())
                    .findFirst()
                    .ifPresent(answer -> answer.setCurrVote(vote.getVote()));
        }
    }

    private List<Integer> getAnswerIds(List<AnswerDTO> answers) {
        return answers
                .stream()
                .map(AnswerDTO::getId)
                .collect(Collectors.toList());
    }
}
