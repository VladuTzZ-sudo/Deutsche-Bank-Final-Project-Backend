package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakenQuizResponseDTO {
    private String courseTitle;
    private String sectionTitle;
    private String quizTitle;
    private int durationQuiz;
    private Long endDateQuiz;
    private String detailsQuiz;
    private Long submittedDate;
    private float quizMark;
    private int isQuizEnded;

}
