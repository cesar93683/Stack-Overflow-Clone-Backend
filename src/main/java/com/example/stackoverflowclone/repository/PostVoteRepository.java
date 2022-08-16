package com.example.stackoverflowclone.repository;

import com.example.stackoverflowclone.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {
    Optional<PostVote> findByUserIdAndPostId(int userId, int postId);
}
