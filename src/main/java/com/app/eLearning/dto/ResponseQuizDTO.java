package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseQuizDTO {
    private int id;
    private String quizName;
    private String description;
    private Boolean isVisible;
}
