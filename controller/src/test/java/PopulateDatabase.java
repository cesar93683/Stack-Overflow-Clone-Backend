import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.dto.QuestionDTO;
import com.example.entity.Answer;
import com.example.entity.Comment;
import com.example.entity.Question;
import com.example.entity.Tag;
import com.example.rest.payload.GenericResponse;
import com.example.rest.payload.auth.LoginRequest;
import com.example.rest.payload.auth.LoginResponse;
import com.example.rest.payload.auth.SignUpRequest;
import com.example.rest.payload.data.CreateAnswerRequest;
import com.example.rest.payload.data.CreateCommentRequest;
import com.example.rest.payload.data.CreateQuestionRequest;
import com.example.rest.payload.data.VoteRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class PopulateDatabase {

    private static final String API_URI_AUTH = "http://localhost:8080/api/auth/";
    private static final String API_URI_QUESTIONS = "http://localhost:8080/api/questions/";
    private static final String API_URI_ANSWERS = "http://localhost:8080/api/answers/";
    private static final String API_URI_COMMENTS = "http://localhost:8080/api/comments/";
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final List<String> tokens = new ArrayList<>();

    public static void main(String[] args) {
        new PopulateDatabase().run();
    }

    private void run() {
        for (String name : readFileToList("names.txt")) {
//            signUpUser(name);
            tokens.add(loginUser(name));
        }
        for (Question question : getQuestions()) {
            String tokenForUserToCreateQuestion = getRandomToken();

            int questionId = createQuestion(tokenForUserToCreateQuestion, question);
            createComments(tokenForUserToCreateQuestion, question.getComments(), questionId, API_URI_QUESTIONS);
            createAnswers(tokenForUserToCreateQuestion, question.getAnswers(), questionId);
        }
    }

    private void createAnswers(String tokenForUserToCreateQuestion, List<Answer> answers, int questionId) {
        List<String> randomTokensExcluding = getRandomTokensExcluding(tokenForUserToCreateQuestion, answers.size());

        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            CreateAnswerRequest createAnswerRequest = new CreateAnswerRequest();
            createAnswerRequest.setContent(answer.getContent());
            createAnswerRequest.setQuestionId(String.valueOf(questionId));

            String tokenForCreateAnswer = randomTokensExcluding.get(i);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenForCreateAnswer);

            HttpEntity<CreateAnswerRequest> requestEntity = new HttpEntity<>(createAnswerRequest, headers);

            AnswerDTO response = REST_TEMPLATE.postForObject(API_URI_ANSWERS, requestEntity, AnswerDTO.class);
            if (response == null) {
                throw new RuntimeException("Error creating answer");
            }

            int answerId = response.getId();

            upVote(tokenForCreateAnswer, answer.getVotes() - 1, answerId, API_URI_ANSWERS + "/vote");

            if (answer.getAccepted() == 1) {
                acceptAnswer(tokenForUserToCreateQuestion, answerId);
            }

            createComments(tokenForUserToCreateQuestion, answer.getComments(), answerId, API_URI_ANSWERS);
        }
    }

    private static void acceptAnswer(String tokenForUserToCreateQuestion, int answerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenForUserToCreateQuestion);

        HttpEntity<CreateAnswerRequest> requestEntity = new HttpEntity<>(null, headers);

        GenericResponse response = REST_TEMPLATE.postForObject(API_URI_ANSWERS + "/accept/" + answerId, requestEntity, GenericResponse.class);
        if (response == null || response.getCode() != 0) {
            throw new RuntimeException("Error creating answer");
        }
    }

    private void createComments(String tokenForUserToCreateQuestion, List<Comment> comments, int id, String apiUri) {
        List<String> randomTokensExcluding = getRandomTokensExcluding(tokenForUserToCreateQuestion, comments.size());

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            CreateCommentRequest createCommentRequest = new CreateCommentRequest();
            createCommentRequest.setContent(comment.getContent());
            createCommentRequest.setId(String.valueOf(id));

            String tokenForCreateQuestion = randomTokensExcluding.get(i);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenForCreateQuestion);

            HttpEntity<CreateCommentRequest> requestEntity = new HttpEntity<>(createCommentRequest, headers);

            CommentDTO response = REST_TEMPLATE.postForObject(apiUri + "/comments", requestEntity, CommentDTO.class);
            if (response == null) {
                throw new RuntimeException("Error creating comment");
            }

            int commentId = response.getId();

            upVote(tokenForCreateQuestion, comment.getVotes() - 1, commentId, API_URI_COMMENTS + "/vote");
        }
    }

    private void upVote(String tokenUsedToCreate, int votesNeeded, int id, String apiUri) {
        List<String> randomTokensExcluding = getRandomTokensExcluding(tokenUsedToCreate, votesNeeded);
        for (String token : randomTokensExcluding) {
            VoteRequest voteRequest = new VoteRequest();
            voteRequest.setVote("1");
            voteRequest.setId(String.valueOf(id));

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<VoteRequest> requestEntity = new HttpEntity<>(voteRequest, headers);

            GenericResponse response = REST_TEMPLATE.postForObject(apiUri, requestEntity, GenericResponse.class);
            if (response == null || response.getCode() != 0) {
                throw new RuntimeException("Error upVoting");
            }
        }
    }

    private List<String> getRandomTokensExcluding(String tokenToExclude, int needed) {
        if (needed > tokens.size() - 1) {
            throw new RuntimeException("Tokens needed is too high[" + needed + "], must be <=" + (tokens.size() - 1));
        }

        List<String> shuffledTokens = new ArrayList<>(tokens);
        Collections.shuffle(shuffledTokens);

        return shuffledTokens.stream()
                .filter(token -> !token.equals(tokenToExclude))
                .limit(needed)
                .collect(Collectors.toList());
    }

    private int createQuestion(String tokenForUserToCreateQuestion, Question question) {
        CreateQuestionRequest createQuestionRequest = new CreateQuestionRequest();
        createQuestionRequest.setTitle(question.getTitle());
        createQuestionRequest.setContent(question.getContent());
        createQuestionRequest.setTags(getTags(question.getTags()));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenForUserToCreateQuestion);

        HttpEntity<CreateQuestionRequest> requestEntity = new HttpEntity<>(createQuestionRequest, headers);

        QuestionDTO response = REST_TEMPLATE.postForObject(API_URI_QUESTIONS, requestEntity, QuestionDTO.class);
        if (response == null) {
            throw new RuntimeException("Error creating question");
        }
        int questionId = response.getId();

        upVote(tokenForUserToCreateQuestion, question.getVotes() - 1, questionId, API_URI_QUESTIONS + "/vote");

        return questionId;
    }

    private String getRandomToken() {
        return tokens.get(new Random().nextInt(tokens.size()));
    }

    private List<String> getTags(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getTag)
                .collect(Collectors.toList());
    }

    private List<Question> getQuestions() {
        try {
            String json = readFileToString("questions.json");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void signUpUser(String name) {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(name + "@gmail.com");
        signUpRequest.setUsername(name);
        signUpRequest.setPassword(name);

        GenericResponse signUpResponse = REST_TEMPLATE.postForObject(API_URI_AUTH + "signup", signUpRequest, GenericResponse.class);
        if (signUpResponse == null || signUpResponse.getCode() != 0) {
            throw new RuntimeException("Error creating user with name: " + name);
        }
    }

    private String loginUser(String name) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(name);
        loginRequest.setPassword(name);
        LoginResponse loginInResponse = REST_TEMPLATE.postForObject(API_URI_AUTH + "login", loginRequest, LoginResponse.class);
        if (loginInResponse == null || loginInResponse.getCode() != 0) {
            throw new RuntimeException("Error logging in user with name: " + name);
        }
        return loginInResponse.getToken();
    }

    private List<String> readFileToList(String fileName) {
        try {
            ClassLoader classLoader = PopulateDatabase.class.getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            Scanner s = new Scanner(file);
            ArrayList<String> list = new ArrayList<>();
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readFileToString(String fileName) {
        try {
            ClassLoader classLoader = PopulateDatabase.class.getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
