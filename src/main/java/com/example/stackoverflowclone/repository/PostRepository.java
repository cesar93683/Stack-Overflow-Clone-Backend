package com.example.stackoverflowclone.repository;

import com.example.stackoverflowclone.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserIdAndPostResponseId(int userId, int postResponseId, Pageable pageable);

    List<Post> findAllByPostResponseId(int postResponseId);

    List<Post> findAllByPostResponseId(int postResponseId, Pageable pageable);
}
