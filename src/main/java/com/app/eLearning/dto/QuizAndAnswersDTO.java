package com.app.eLearning.dto;

import com.app.eLearning.dao.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAndAnswersDTO {

    private int id;

    private String quizName;

    private String description;

    private Boolean isVisible = false;

    private Long deadline;

    private int duration;

    private Set<Question> questions;

}
