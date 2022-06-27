package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "given_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GivenAnswer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// id-ul raspunsului din baza de date
	private int id_answer;

	public GivenAnswer(int id_answer) {
		this.id_answer = id_answer;
	}
}
