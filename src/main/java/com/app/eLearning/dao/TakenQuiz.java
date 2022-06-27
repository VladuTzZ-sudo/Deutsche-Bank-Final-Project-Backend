package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "taken_quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakenQuiz
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "taken_quiz_id", referencedColumnName = "id")
	Set<GivenAnswer> givenAnswers;

	@Column(name = "submitted_date")
	@Temporal(TemporalType.DATE)
	private Date submittedDate;

	@OneToOne(cascade = CascadeType.ALL)
	private Quiz quiz;

	private float grade;


}
