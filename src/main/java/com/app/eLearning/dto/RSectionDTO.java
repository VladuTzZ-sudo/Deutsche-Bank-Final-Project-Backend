package com.app.eLearning.dto;

import com.app.eLearning.dao.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSectionDTO {

    private String title;
    private String description;
    private String quizName;
    private String quizDescription;

    List<RContentDTO> rContentDTOList;

}
