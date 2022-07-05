package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RCourseDTO {

    private String name;
    private String teacherName;
    private String description;

    List<RSectionDTO> rSectionDTOList;

}
