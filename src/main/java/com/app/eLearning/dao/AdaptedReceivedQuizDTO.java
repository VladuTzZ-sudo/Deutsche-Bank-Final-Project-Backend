package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdaptedReceivedQuizDTO {
    private String quizzTitle;
    private String details;
    private Long due;
    private int duration;
    private Set<AdaptedQuestionsDTO> questions;
}
