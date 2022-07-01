package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularCourseDTO {
    private String courseName;
    private int totalTakenQuizzes;

    public void increaseCoursePopulairty(){
        this.totalTakenQuizzes ++;
    }
}
