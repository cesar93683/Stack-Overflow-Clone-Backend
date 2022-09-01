package com.example.repository;

import com.example.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Optional<Vote> findByUserIdAndQuestionId(int userId, int questionId);

    Optional<Vote> findByUserIdAndAnswerId(int userId, int answerId);

    Optional<Vote> findByUserIdAndCommentId(int userId, int commentId);

    List<Vote> findByUserIdAndQuestionIdIn(int userId, List<Integer> questionIds);

    List<Vote> findByUserIdAndAnswerIdIn(int userId, List<Integer> answerIds);

    List<Vote> findByUserIdAndCommentIdIn(int userId, List<Integer> commentIds);
}
