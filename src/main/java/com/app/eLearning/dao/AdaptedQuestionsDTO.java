package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdaptedQuestionsDTO {
    private String questionText;
    private Set<AdaptedAnswersDTO> answers;
}
