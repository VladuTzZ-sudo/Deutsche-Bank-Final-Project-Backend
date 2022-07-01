package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// name of the file
	private String name;

	// creation date in ms
	private long date;

	// the id of the user who has created the file
	@Column(name = "user_id")
	private int userId;

	// the id of the course
	@Column(name = "course_id")
	private int courseId;

	// the id of the section

	@Column(name = "section_id")
	private int sectionId;


}
