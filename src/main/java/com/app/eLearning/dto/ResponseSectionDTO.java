package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSectionDTO {
    private int id;
    private String title;
    private String description;
    private ResponseQuizDTO quiz;
}
