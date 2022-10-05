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

import static com.example.service.Utils.*;
import static com.example.utils.Constants.UP_VOTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        User user = getUser();
        Question question = new Question();
        Answer answer = new Answer(content, user, new Question());

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

        User user = getUser();
        Question question = getQuestion();
        Answer answer = new Answer(content, user, question);

        int numAnswers = question.getNumAnswers();

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
        int id = 1;

        Mockito.when(mockAnswerRepository.findById(id)).thenReturn(Optional.of(getQuestion().getAnswers().get(0)));

        AnswerDTO answerDTO = answerService.getAnswer(id);

        AnswerDTO expectedAnswerDTO = getQuestionDTO().getAnswers().get(0);
        expectedAnswerDTO.setComments(new ArrayList<>());

        assertThat(answerDTO).usingRecursiveComparison().isEqualTo(expectedAnswerDTO);
    }

}