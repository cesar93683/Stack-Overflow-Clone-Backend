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

    @Column(name = "description", nullable = false, updatable = false)
    private String description;

    @Column(name = "num_questions", nullable = false)
    private String numQuestions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(String numQuestions) {
        this.numQuestions = numQuestions;
    }
}
