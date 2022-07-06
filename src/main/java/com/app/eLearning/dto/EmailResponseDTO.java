package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponseDTO {
    private String quizName;
    private List<String> studentsGrades;

    @Override
    public String toString() {
        return "EmailResponseDTO{" +
                "quizName='" + quizName + '\'' +
                ", studentsGrades=" + studentsGrades +
                '}';
    }
}
