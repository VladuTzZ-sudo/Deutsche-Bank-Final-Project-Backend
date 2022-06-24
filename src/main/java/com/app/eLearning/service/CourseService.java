package com.app.eLearning.service;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dao.User;
import com.app.eLearning.dto.CourseResponseDTO;
import com.app.eLearning.dto.CreateCourseDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
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

//	public List<CourseResponseDTO> getAllCourses(Pair<Integer, String> authData)
//	{
//		if (authData == null)
//			return null;
//
//		User user = userRepository.findById(authData.getFirst()).get();
//		List<Course> userCourses = user.getUserCourses();
//
//		List<CourseResponseDTO> courseDTOList = new ArrayList<>();
//		for (int i = 0; i < userCourses.size(); i++)
//		{
//			Course currentCourse = userCourses.get(i);
//			courseDTOList.add(new CourseResponseDTO(currentCourse.getId(),
//					currentCourse.getName(), currentCourse.getTeacherName(),
//					currentCourse.getDescription()));
//		}
//		return courseDTOList;
//	}

	public List<CourseResponseDTO> getAllCourses(){

		List<CourseResponseDTO> courseResponseDTOList = new ArrayList<>();

		try {
			List<Course> courseList = courseRepository.findAll();
			if (courseList == null){
				return null;
			}

			for (Course c:courseList) {
				courseResponseDTOList.add(new CourseResponseDTO(c.getId(), c.getName(), c.getTeacherName(), c.getDescription()));
			}

			return courseResponseDTOList;

		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}

	public CourseResponseDTO getCourse(Pair<Integer, String> authData, Integer id) throws CourseNotFoundException
	{
		if (authData == null)
			return null;

		// caut utilizatorul in baza de date si caut cursul cu id-ul id in lista lui de cursuri
		User user = userRepository.findById(authData.getFirst()).get();
		List<Course> userCourses = user.getUserCourses();
		CourseResponseDTO response = null;
		// iterez prin lista de cursuri ale user-ului si caut cursul cu id ul primit ca parametru
		for (int i = 0; i < userCourses.size(); i++)
		{
			Course currentCourse = userCourses.get(i);
			if (currentCourse.getId() == id)
			{
				response = new CourseResponseDTO();
				response.setId(currentCourse.getId());
				response.setName(currentCourse.getName());
				response.setTeacherName(currentCourse.getTeacherName());
				response.setDescription(currentCourse.getDescription());
				break;
			}
		}
		if (response == null)
		{
			throw new CourseNotFoundException();
		}
		return response;
	}

	public ResponseEntity<String> postCourse(Pair<Integer, String> authPair, CreateCourseDTO createCourseDTO)
	{
		// crearea unui nou curs
		Course course = new Course();
		course.setName(createCourseDTO.getName());
		course.setDescription(createCourseDTO.getDescription());

		// se cauta teacher-ul in baza de date si i se ia numele
		User teacher = userRepository.findById(authPair.getFirst()).get();

		// se seteaza numele teacher-ului
		course.setTeacherName(teacher.getName());

		// se adauga cursul teacher-ului
		teacher.getUserCourses().add(course);

		//problema curs duplicat - ne intereseaza ca un curs sa fie adaugat de profesor, dar accesarea cursurilor se face separat de oricine
//		// adaugarea cursului la toti userii studenti
//		List<User> users = userRepository.findAll();
//		for (int i = 0; i < users.size(); i++)
//		{
//			User currentUser = users.get(i);
//			if (currentUser.getUserRole().getRoleId() == 1)
//			{
//				currentUser.getUserCourses().add(course);
//				userRepository.save(currentUser);
//			}
//		}

		// salvarea profesorului in baza de date
		userRepository.save(teacher);

		return new ResponseEntity<String>("Course added successfully", HttpStatus.OK);
	}

}
