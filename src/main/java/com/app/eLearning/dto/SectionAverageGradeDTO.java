package com.app.eLearning.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionAverageGradeDTO {
    private String sectionName;
    private float averageGrade;
}
