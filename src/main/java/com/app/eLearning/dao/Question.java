package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content_question")
    private String contentQuestion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_question", referencedColumnName = "id")
    private Set<Answer> answers;

    public Question(String contentQuestion, Set<Answer> answers) {
        this.contentQuestion = contentQuestion;
        this.answers = answers;
    }
}
