package com.app.eLearning.repository;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dao.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer>
{
	public Course findFirstById(int courseId);
}