package com.app.eLearning.dto;

import com.app.eLearning.dao.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewSectionDTO {
    private String token;
    private Section section;
}
