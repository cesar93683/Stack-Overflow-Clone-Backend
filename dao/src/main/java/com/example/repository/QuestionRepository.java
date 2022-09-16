package com.example.repository;

import com.example.entity.Question;
import com.example.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAllByUserId(int userId, Pageable pageable);
    Page<Question> findByTags(Tag tag, Pageable pageable);
}
