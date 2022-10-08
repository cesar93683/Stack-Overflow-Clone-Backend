package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.entity.Answer;
import com.example.entity.Question;
import com.example.entity.User;
import com.example.entity.Vote;
import com.example.exceptions.ServiceException;
import com.example.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.service.Utils.getQuestion;
import static com.example.service.Utils.getQuestionDTO;
import static com.example.utils.Constants.UP_VOTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @InjectMocks
    private AnswerServiceImpl answerService;
    @Mock
    private QuestionRepository mockQuestionRepository;
    @Mock
    private AnswerRepository mockAnswerRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private VoteRepository mockVoteRepository;
    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        answerService = new AnswerServiceImpl();
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void createAnswerShouldThrowAnExceptionIfTheUserIsNotFound() {
        String content = "the content";
        int userId = 1;
        int questionId = 2;

        Mockito.when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.createAnswer(content, userId, questionId)
        );

        assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test
    public void createAnswerShouldThrowAnExceptionIfTheQuestionIsNotFound() {
        String content = "the content";
        int userId = 1;
        int questionId = 2;
        User user = new User();

        Mockito.when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(mockQuestionRepository.findById(questionId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.createAnswer(content, userId, questionId)
        );

        assertEquals("Question not found with id: " + questionId, exception.getMessage());
    }

    @Test
    public void createAnswerShouldThrowAnExceptionIfTheUserHasAlreadyRespondedToTheQuestion() {
        String content = "the content";
        int userId = 1;
        int questionId = 2;

        User user = new User();
        user.setId(userId);
        Question question = new Question();
        Answer answer = new Answer(content, user, question);

        Mockito.when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(mockQuestionRepository.findById(questionId)).thenReturn(Optional.of(question));
        Mockito.when(mockAnswerRepository.findAllByQuestionId(questionId)).thenReturn(List.of(answer));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.createAnswer(content, userId, questionId)
        );

        assertEquals("User with id 1 has already answer question with id: " + questionId, exception.getMessage());
    }

    @Test
    public void createAnswerHappyPath() throws ServiceException {
        String content = "the content";
        int userId = 1;
        int questionId = 2;
        int numAnswers = 3;

        User user = new User();
        user.setId(userId);
        Question question = new Question();
        question.setId(questionId);
        question.setNumAnswers(numAnswers);
        Answer answer = new Answer(content, user, question);

        AnswerDTO answerDTOExpected = new AnswerDTO(answer, false);
        answerDTOExpected.setCurrVote(UP_VOTE);

        Mockito.when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(mockQuestionRepository.findById(questionId)).thenReturn(Optional.of(question));
        Mockito.when(mockAnswerRepository.findAllByQuestionId(questionId)).thenReturn(new ArrayList<>());
        Mockito.when(mockAnswerRepository.save(any())).thenReturn(answer);

        AnswerDTO answerDTOActual = answerService.createAnswer(content, userId, questionId);

        verify(mockQuestionRepository).save(question);
        verify(mockVoteRepository).save(new Vote(user, null, answer, null, UP_VOTE));
        assertEquals(numAnswers + 1, question.getNumAnswers());
        assertEquals(answerDTOExpected, answerDTOActual);
    }

    @Test
    public void getAnswerShouldThrowAnExceptionIfTheAnswerIsNotFound() {
        int id = 1;

        Mockito.when(mockAnswerRepository.findById(id)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.getAnswer(id)
        );

        assertEquals("Answer not found with id: " + id, exception.getMessage());
    }

    @Test
    public void getAnswerShouldReturnTheAnswerIfFound() throws ServiceException {
        Answer answer = getQuestion().getAnswers().get(0);
        int answerId = answer.getId();

        AnswerDTO expectedAnswerDTO = getQuestionDTO().getAnswers().get(0);
        expectedAnswerDTO.setComments(new ArrayList<>());

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        AnswerDTO answerDTO = answerService.getAnswer(answerId);

        assertThat(answerDTO).usingRecursiveComparison().isEqualTo(expectedAnswerDTO);
    }

    @Test
    public void updateAnswerShouldThrowAnExceptionIfTheAnswerIsNotFound() {
        int answerId = 1;
        String content = "content";
        int userId = 2;

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.updateAnswer(answerId, content, userId)
        );

        assertEquals("Answer not found with id: " + answerId, exception.getMessage());
    }

    @Test
    public void updateAnswerShouldThrowAnExceptionIfAUserTriesToUpdateAnAnswerTheyDidNotCreate() {
        int answerId = 1;
        String content = "content";
        int userId = 2;
        Answer answer = new Answer();
        User user = new User();
        user.setId(1);
        answer.setUser(user);

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.updateAnswer(answerId, content, userId)
        );

        assertEquals("User with id: " + userId + " did not create answer with id: " + answerId, exception.getMessage());
    }

    @Test
    public void updateAnswerHappyPath() throws ServiceException {
        int answerId = 1;
        String content = "content";
        int userId = 2;
        Answer answer = new Answer();
        User user = new User();
        user.setId(userId);
        answer.setUser(user);

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        answerService.updateAnswer(answerId, content, userId);

        assertEquals(content, answer.getContent());
        assertNotNull(answer.getUpdatedAt());
        verify(mockAnswerRepository).save(answer);
    }

    @Test
    public void deleteAnswerShouldThrowAnExceptionIfTheAnswerIsNotFound() {
        int answerId = 1;
        int userId = 2;

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.deleteAnswer(answerId, userId)
        );

        assertEquals("Answer not found with id: " + answerId, exception.getMessage());
    }

    @Test
    public void deleteAnswerShouldThrowAnExceptionIfAUserTriesToUpdateAnAnswerTheyDidNotCreate() {
        int answerId = 1;
        int userId = 2;
        Answer answer = new Answer();
        User user = new User();
        user.setId(1);
        answer.setUser(user);

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.deleteAnswer(answerId, userId)
        );

        assertEquals("User with id: " + userId + " did not create answer with id: " + answerId, exception.getMessage());
    }

    @Test
    public void deleteAnswerHappyPath() throws ServiceException {
        int answerId = 1;
        int userId = 2;
        Answer answer = new Answer();
        User user = new User();
        user.setId(userId);
        answer.setUser(user);
        Question question = new Question();
        question.setNumAnswers(5);
        answer.setQuestion(question);

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        answerService.deleteAnswer(answerId, userId);

        assertEquals(4, question.getNumAnswers());
        verify(mockQuestionRepository).save(question);
        verify(mockAnswerRepository).delete(answer);
    }

    @Test
    public void acceptAnswerShouldThrowAnExceptionIfTheAnswerDoesNotExist() {
        int answerId = 1;
        int userId = 2;

        Mockito.when(mockAnswerRepository.findById(answerId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.acceptAnswer(answerId, userId)
        );

        assertEquals("Answer not found with id: " + answerId, exception.getMessage());
    }

}