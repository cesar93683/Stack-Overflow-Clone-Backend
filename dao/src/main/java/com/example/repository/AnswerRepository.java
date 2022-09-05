package com.example.repository;

import com.example.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findAllByUserId(int userId, Pageable pageable);

    List<Answer> findAllByQuestionId(int questionId);
}
