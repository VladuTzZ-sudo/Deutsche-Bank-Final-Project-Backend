package com.app.eLearning.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO {
    private int answerId;
    private String answerContent;
    private boolean correctAnswer;
    private boolean userAnswer;

    public AnswerResponseDTO(int answerId, String answerContent, boolean correctAnswer) {
        this.answerId = answerId;
        this.answerContent = answerContent;
        this.correctAnswer = correctAnswer;
    }
}
