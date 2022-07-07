package com.app.eLearning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponseDTO
{
	private int quizId;
	private String quizName;
	private List<String> studentsGrades;

	@Override
	public String toString()
	{
//        return "Quiz is over. \n \n" + "Quiz Name: " + quizName  + "\n" + "Students and grades: \n" + studentsGrades.toString() + "\n";
		StringBuilder sb = new StringBuilder();
		sb.append("The quiz " + quizName + " is over!\n\n");
		sb.append("The results are:\n");
		for (int i = 0; i < studentsGrades.size(); i++)
		{
            sb.append(studentsGrades.get(i) + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

}
