package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quiz_name")
    private String quizName;

    @Column(name = "is_visible")
    private Boolean isVisible = false;

    private Date deadline;

    private int duration;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_quiz", referencedColumnName = "id")
    private Set<Question> questions;

    public Quiz(Set<Question> questions) {
        this.questions = questions;
    }

    public Quiz(String quizName, boolean isVisible, Set<Question> questions) {
        this.quizName = quizName;
        this.isVisible = isVisible;
        this.questions = questions;
    }
}
