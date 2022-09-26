package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.exceptions.ServiceException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired
    private TagRepository tagRepository;

    @Override
    public QuestionsDTO getQuestions(int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        Page<Question> pageQuestion = questionRepository.findAll(pageable);
        List<QuestionDTO> questions = pageQuestion
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
        return new QuestionsDTO(pageQuestion.getTotalPages(), questions);
    }

    @Override
    public QuestionsDTO getQuestionsByUserId(int userId, int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        Page<Question> pageQuestion = questionRepository.findAllByUserId(userId, pageable);
        List<QuestionDTO> questions = pageQuestion
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
        return new QuestionsDTO(pageQuestion.getTotalPages(), questions);
    }

    @Override
    public QuestionsDTO getQuestionsAnsweredByUserId(int userId, int page, boolean sortedByVotes) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        Page<Answer> pageQuestion = answerRepository.findAllByUserId(userId, pageable);
        List<QuestionDTO> questions = pageQuestion
                .stream()
                .map((Answer answer) -> new QuestionDTO(answer.getQuestion(), false))
                .collect(Collectors.toList());
        return new QuestionsDTO(pageQuestion.getTotalPages(), questions);
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
    public QuestionDTO createQuestion(String title, String content, List<String> tagTypes, int userId) throws ServiceException {
        List<Tag> tags = tagRepository.findByTagIn(tagTypes);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.save(new Question(title, content, user, tags));
        voteRepository.save(new Vote(user, question, null, null, UP_VOTE));
        for (Tag tag : tags) {
            tag.setNumQuestions(tag.getNumQuestions() + 1);
        }
        tagRepository.saveAll(tags);
        QuestionDTO questionDTO = new QuestionDTO(question, false);
        questionDTO.setCurrVote(UP_VOTE);
        return questionDTO;
    }

    @Override
    public void updateQuestion(int questionId, String content, List<String> tagTypes, int userId) throws ServiceException {
        List<Tag> tags = tagRepository.findByTagIn(tagTypes);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        if (question.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create question with id: " + questionId);
        }
        updateTags(tags, question.getTags());
        question.setContent(content);
        question.setTags(tags);
        question.setUpdatedAt(new Date());
        questionRepository.save(question);
    }

    private void updateTags(List<Tag> updatedTags, List<Tag> currentTags) {
        List<Tag> newTags = new ArrayList<>();
        for (Tag updatedTag : updatedTags) {
            boolean found = currentTags.stream()
                    .anyMatch(currentTag -> updatedTag == currentTag);
            if (!found) {
                newTags.add(updatedTag);
            }
        }
        List<Tag> removedTags = new ArrayList<>();
        for (Tag currentTag : currentTags) {
            boolean found = updatedTags.stream()
                    .anyMatch(updatedTag -> currentTag == updatedTag);
            if (!found) {
                removedTags.add(currentTag);
            }
        }
        if (!newTags.isEmpty()) {
            for (Tag tag : newTags) {
                tag.setNumQuestions(tag.getNumQuestions() + 1);
            }
            tagRepository.saveAll(newTags);
        }
        if (!removedTags.isEmpty()) {
            for (Tag tag : removedTags) {
                tag.setNumQuestions(tag.getNumQuestions() - 1);
            }
            tagRepository.saveAll(removedTags);
        }
    }

    @Override
    public void deleteQuestion(int questionId, int userId) throws ServiceException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        if (question.getUser().getId() != userId) {
            throw new ServiceException("User with id: " + userId + " did not create question with id: " + questionId);
        }
        for (Tag tag : question.getTags()) {
            tag.setNumQuestions(tag.getNumQuestions() - 1);
        }
        tagRepository.saveAll(question.getTags());
        questionRepository.delete(question);
    }

    @Override
    public void voteQuestion(int userId, int questionId, int newVoteType) throws ServiceException {
        User userVoter = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        Vote vote = voteRepository.findByUserIdAndQuestionId(userVoter.getId(), questionId)
                .orElse(new Vote(userVoter, question, null, null, NEUTRAL));
        int oldVoteType = vote.getVote();
        if (newVoteType == oldVoteType) {
            return;
        }
        question.setVotes(question.getVotes() + getVoteDiff(vote.getVote(), newVoteType));
        vote.setVote(newVoteType);
        User userQuestion = question.getUser();
        if (userVoter != userQuestion) {
            updateReputation(newVoteType, oldVoteType, userQuestion);
        }

        questionRepository.save(question);
        voteRepository.save(vote);
    }

    @Override
    public CommentDTO createComment(String content, int questionId, int userId) throws ServiceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found with id: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException("Question not found with id: " + questionId));
        Comment comment = commentRepository.save(new Comment(content, user, question, null));
        voteRepository.save(new Vote(user, null, null, comment, UP_VOTE));
        CommentDTO commentDTO = new CommentDTO(comment);
        commentDTO.setCurrVote(UP_VOTE);
        return commentDTO;
    }

    @Override
    public List<TagDTO> getTags() {
        List<TagDTO> tags = new ArrayList<>();
        for (Tag tag : tagRepository.findAll()) {
            tags.add(new TagDTO(tag));
        }
        return tags;
    }

    @Override
    public QuestionsAndTagDTO getQuestionByTag(String tagType, int page, boolean sortedByVotes) {
        Tag tag = tagRepository.findByTag(tagType)
                .orElseThrow(() -> new org.hibernate.service.spi.ServiceException("Tag not found with tag: " + tagType));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortedByVotes ? "votes" : "id").descending());
        Page<Question> pageQuestion = questionRepository.findByTags(tag, pageable);
        List<QuestionDTO> questions = pageQuestion
                .stream()
                .map((Question question) -> new QuestionDTO(question, false))
                .collect(Collectors.toList());
        return new QuestionsAndTagDTO(
                new QuestionsDTO(pageQuestion.getTotalPages(), questions),
                new TagDTO(tag));
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

    private void updateReputation(int newVoteType, int oldVoteType, User userAnswer) {
        if (newVoteType == UP_VOTE) {
            if (oldVoteType == NEUTRAL) {
                userAnswer.setReputation(userAnswer.getReputation() + 10);
            } else { // DOWN_VOTE
                userAnswer.setReputation(userAnswer.getReputation() + 12);
            }
        } else if (newVoteType == NEUTRAL) {
            if (oldVoteType == UP_VOTE) {
                userAnswer.setReputation(userAnswer.getReputation() - 10);
            } else { // DOWN_VOTE
                userAnswer.setReputation(userAnswer.getReputation() + 2);
            }
        } else { // DOWN_VOTE
            if (oldVoteType == UP_VOTE) {
                userAnswer.setReputation(userAnswer.getReputation() - 12);
            } else { // NEUTRAL
                userAnswer.setReputation(userAnswer.getReputation() - 2);
            }
        }
    }
}
