package com.app.eLearning.dto;

import com.app.eLearning.dao.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {

    private String contentQuestion;

    private Set<AnswerResponseDTO> answers;

    public QuestionResponseDTO(String contentQuestion) {
        this.contentQuestion = contentQuestion;
    }

}
