package com.example.stackoverflowclone.repository;

import com.example.stackoverflowclone.entity.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostResponseRepository extends JpaRepository<PostResponse, Integer> {
}
