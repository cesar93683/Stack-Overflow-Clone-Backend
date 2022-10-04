package com.example.service;

import com.example.dto.AnswerDTO;
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
import java.util.Optional;

import static com.example.service.Utils.getQuestion;
import static com.example.service.Utils.getQuestionDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void getAnswerShouldThrowExceptionIfAnswerNotFound() {
        int id = 1;

        Mockito.when(mockAnswerRepository.findById(id)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.getAnswer(id)
        );

        assertEquals("Answer not found with id: 1", exception.getMessage());
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

    @Test
    public void createAnswerShouldThrowAnExceptionIfTheUserIsNotFound() {
        String content = "the content";
        int userId = 1;
        int questionId = 5;

        Mockito.when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> answerService.createAnswer(content, userId, questionId)
        );

        assertEquals("User not found with id: 1", exception.getMessage());
    }

}