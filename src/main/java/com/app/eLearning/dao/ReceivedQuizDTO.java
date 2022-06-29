package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedQuizDTO {

    private String quizName;

    private String description;

    private Boolean isVisible = false;

    private Long deadline;

    private int duration;

    private Set<Question> questions;

}
