package com.example.service;

import com.example.dto.AnswerDTO;
import com.example.dto.CommentDTO;
import com.example.dto.QuestionDTO;
import com.example.dto.UserDTO;
import com.example.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    private static final String TAG = "tag";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String USERNAME = "username";
    private static final int ID = 1;
    private static final int VOTES = 1;
    private static final int REPUTATION = 1;

    public static QuestionDTO getQuestionDTO() {
        QuestionDTO question = new QuestionDTO();
        question.setId(ID);
        question.setTitle(TITLE);
        question.setContent(CONTENT);
        question.setVotes(VOTES);
        question.setAnswers(getAnswersDTO());
        question.setNumAnswers(question.getAnswers().size());
        for (AnswerDTO answerDTO : question.getAnswers()) {
            answerDTO.setQuestionId(question.getId());
        }
        question.setTags(getTagsStrings());
        question.setUser(getUserDTO());
        question.setComments(getCommentsDTO());
        return question;
    }

    private static List<AnswerDTO> getAnswersDTO() {
        List<AnswerDTO> answers = new ArrayList<>();
        answers.add(getAnswerDTOAccepted());
        answers.add(getAnswerDTONotAccepted());
        return answers;
    }

    private static AnswerDTO getAnswerDTOAccepted() {
        AnswerDTO answer = new AnswerDTO();
        answer.setId(ID);
        answer.setContent(CONTENT);
        answer.setVotes(VOTES);
        answer.setAccepted(1);
        answer.setComments(getCommentsDTO());
        answer.setUser(getUserDTO());
        answer.setCurrVote(0);
        answer.setCreatedAt(getDate());
        answer.setUpdatedAt(getDate());
        return answer;
    }

    private static AnswerDTO getAnswerDTONotAccepted() {
        AnswerDTO answer = getAnswerDTOAccepted();
        answer.setAccepted(0);
        return answer;
    }

    private static UserDTO getUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setReputation(REPUTATION);
        return user;
    }

    private static List<String> getTagsStrings() {
        List<String> tags = new ArrayList<>();
        tags.add(TAG);
        return tags;
    }

    private static List<CommentDTO> getCommentsDTO() {
        List<CommentDTO> comments = new ArrayList<>();
        comments.add(getCommentDTO());
        comments.add(getCommentDTO());
        return comments;
    }

    private static CommentDTO getCommentDTO() {
        CommentDTO comment = new CommentDTO();
        comment.setId(ID);
        comment.setContent(CONTENT);
        comment.setVotes(VOTES);
        comment.setCurrVote(0);
        comment.setUser(getUserDTO());
        comment.setCreatedAt(getDate());
        return comment;
    }

    public static Question getQuestion() {
        Question question = new Question();
        question.setId(ID);
        question.setTitle(TITLE);
        question.setContent(CONTENT);
        question.setVotes(VOTES);
        question.setAnswers(getAnswers());
        question.setNumAnswers(question.getAnswers().size());
        for (Answer answer : question.getAnswers()) {
            answer.setQuestion(question);
        }
        question.setTags(getTags());
        question.setUser(getUser());
        question.setComments(getComments());
        for (Comment comment : question.getComments()) {
            comment.setQuestion(question);
        }
        question.setVoteList(getVoteList());
        for (Vote vote : question.getVoteList()) {
            vote.setQuestion(question);
        }
        return question;
    }

    private static List<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();
        answers.add(getAnswerAccepted());
        answers.add(getAnswerNotAccepted());
        return answers;
    }

    public static Answer getAnswerAccepted() {
        Answer answer = new Answer();
        answer.setId(ID);
        answer.setContent(CONTENT);
        answer.setVotes(VOTES);
        answer.setAccepted(1);
        answer.setCreatedAt(getDate());
        answer.setUpdatedAt(getDate());
        answer.setUser(getUser());
        answer.setComments(getComments());
        for (Comment comment : answer.getComments()) {
            comment.setAnswer(answer);
        }
        answer.setVoteList(getVoteList());
        for (Vote vote : answer.getVoteList()) {
            vote.setAnswer(answer);
        }
        return answer;
    }

    public static Answer getAnswerNotAccepted() {
        Answer answer = getAnswerAccepted();
        answer.setAccepted(0);
        return answer;
    }

    private static List<Vote> getVoteList() {
        List<Vote> votes = new ArrayList<>();
        votes.add(getVote());
        return votes;
    }

    private static Vote getVote() {
        Vote vote = new Vote();
        vote.setId(ID);
        vote.setVote(1);
        vote.setUser(getUser());
        return vote;
    }

    private static User getUser() {
        User user = new User();
        user.setId(ID);
        user.setEmail("email");
        user.setUsername(USERNAME);
        user.setPassword("password");
        user.setReputation(REPUTATION);
        return user;
    }

    private static List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(getTag());
        tags.add(getTag());
        return tags;
    }

    private static Tag getTag() {
        Tag tag = new Tag();
        tag.setId(ID);
        tag.setTag(TAG);
        tag.setDescription("description");
        tag.setNumQuestions(1);
        return tag;
    }

    private static List<Comment> getComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(getComment());
        comments.add(getComment());
        return comments;
    }

    private static Comment getComment() {
        Comment comment = new Comment();
        comment.setId(ID);
        comment.setContent(CONTENT);
        comment.setVotes(VOTES);
        comment.setCreatedAt(getDate());
        comment.setUser(getUser());
        comment.setVoteList(getVoteList());
        for (Vote vote : comment.getVoteList()) {
            vote.setComment(comment);
        }
        return comment;
    }

    public static Date getDate() {
        return new Date(0);
    }

}
