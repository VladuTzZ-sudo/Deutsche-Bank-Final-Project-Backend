package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSectionDTO {
    private int id;
    private String title;
    private String description;
    private ResponseQuizDTO quiz;
    private List<FileDTO> files;
}
