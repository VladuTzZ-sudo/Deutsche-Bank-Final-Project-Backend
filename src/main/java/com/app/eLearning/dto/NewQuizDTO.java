package com.app.eLearning.dto;

import com.app.eLearning.dao.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewQuizDTO {
    private String token;
    private int sectionID;
    private Quiz quiz;
}
