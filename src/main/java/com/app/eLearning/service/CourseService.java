package com.app.eLearning.service;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dao.User;
import com.app.eLearning.dto.CourseResponseDTO;
import com.app.eLearning.dto.CreateCourseDTO;
import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService
{
	@Autowired
	CourseRepository courseRepository;

	@Autowired
	UserRepository userRepository;

	public List<CourseResponseDTO> getAllCourses(Pair<Integer, String> authData)
	{
		if (authData == null)
			return null;

		User user = userRepository.findById(authData.getFirst()).get();
		List<Course> userCourses = user.getUserCourses();

		List<CourseResponseDTO> courseDTOList = new ArrayList<>();
		for (int i = 0; i < userCourses.size(); i++)
		{
			Course currentCourse = userCourses.get(i);
			courseDTOList.add(new CourseResponseDTO(currentCourse.getId(),
					currentCourse.getName(), currentCourse.getTeacherName()));
		}
		return courseDTOList;
	}

	public ResponseEntity<String> postCourse(Pair<Integer, String> authPair, CreateCourseDTO createCourseDTO)
	{
		// crearea unui nou curs
		Course course = new Course();
		course.setName(createCourseDTO.getCourseName());
		course.setTeacherName(createCourseDTO.getTeacherName());

		User teacher = userRepository.findById(authPair.getFirst()).get();
		teacher.getUserCourses().add(course);

		// adaugarea cursului la toti userii studenti
		List<User> users = userRepository.findAll();
		for (int i = 0; i < users.size(); i++)
		{
			User currentUser = users.get(i);
			if(currentUser.getUserRole().getRoleId() == 1)
			{
				currentUser.getUserCourses().add(course);
				userRepository.save(currentUser);
			}
		}

		// salvarea profesorului in baza de date
		userRepository.save(teacher);

		return new ResponseEntity<String>("Course added successfully", HttpStatus.OK);
	}

}
