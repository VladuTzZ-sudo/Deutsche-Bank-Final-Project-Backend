package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO
{
	private String subjectTitle;
	private String sectionTitle;
	private String quizTitle;
	private int duration;
	private long endDate;
	private String details;
}
