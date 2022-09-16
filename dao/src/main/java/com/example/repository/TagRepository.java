package com.example.repository;

import com.example.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByTagIn(List<String> tags);
    Optional<Tag> findByTag(String tags);
}
