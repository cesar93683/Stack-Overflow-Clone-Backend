package com.example.stackoverflowclone.repository;

import com.example.stackoverflowclone.entity.PostResponseVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostResponseVoteRepository extends JpaRepository<PostResponseVote, Integer> {
    Optional<PostResponseVote> findByUserIdAndPostResponseId(int userId, int postResponseId);
}
