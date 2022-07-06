package com.app.eLearning.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String teacherName;
	private String description;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id", referencedColumnName = "id")
	List<Section> courseSections;

	public Course(String name)
	{
		this.name = name;
	}

	public void addSection(Section section){
		this.courseSections.add(section);
	}
}
