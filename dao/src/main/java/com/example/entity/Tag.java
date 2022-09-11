package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "tag", nullable = false, updatable = false)
    private String tag;

    @Column(name = "content", nullable = false, updatable = false)
    private String content;

    @Column(name = "num_questions", nullable = false, updatable = false)
    private String numQuestions;

}
