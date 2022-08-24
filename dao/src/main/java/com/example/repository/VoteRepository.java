package com.example.repository;

import com.example.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Optional<Vote> findByUserIdAndPostId(int userId, int postId);
    Optional<Vote> findByUserIdAndCommentId(int userId, int commentId);
}