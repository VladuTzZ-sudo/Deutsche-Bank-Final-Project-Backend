package com.app.eLearning.controller;

import com.app.eLearning.dto.CourseResponseDTO;
import com.app.eLearning.dto.CreateCourseDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.CourseService;
import com.app.eLearning.service.UserService;
import com.app.eLearning.utils.LoginAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class CourseController {
	@Autowired
	CourseService courseService;

	@Autowired
	UserService userService;

//	@GetMapping("/courses")
//	@ResponseBody
//	public List<CourseResponseDTO> getAllCourses(@RequestBody String token) //!!! @RequestHeader nu body pe GET
//	{
//		Pair<Integer, String> loginAuth = null;
//		try
//		{
//			loginAuth = LoginAuthorization.validateAuthorization(token);
//		}
//		catch (WrongTokenException e)
//		{
//			e.printStackTrace();
//		}
//
//		return courseService.getAllCourses(loginAuth);
//	}

	@GetMapping("/courses")
	@ResponseBody
	public List<CourseResponseDTO> getAllCourses(@RequestHeader ("Authorization") String authHeader) throws WrongTokenException {
		Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		if (!userService.checkIfUserExists(loginAuth.getFirst())) {
			return null;
		}

		return courseService.getAllCourses();
	}


	@GetMapping("/courses/{id}")
	@ResponseBody
	public CourseResponseDTO getCourse(@PathVariable(required = true, name = "id") Integer id,
									   @RequestHeader ("Authorization") String authHeader) throws CourseNotFoundException
	{
		Pair<Integer, String> loginAuth = null;
		try
		{
			String token = authHeader.substring(7);
			loginAuth = LoginAuthorization.validateAuthorization(token);
		}
		catch (WrongTokenException e)
		{
			e.printStackTrace();
		}
		return courseService.getCourse(loginAuth, id);
	}

	// endpoint creare curs de catre profesor
	@PostMapping("/courses")
	public ResponseEntity<String> postCourse(@RequestBody CreateCourseDTO dto, @RequestHeader ("Authorization") String authHeader) throws WrongTokenException {
		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		assert loginAuth != null;
		if (!loginAuth.getSecond().equals("teacher"))
		{
			return new ResponseEntity<>("You are not authorized to create a new course!", HttpStatus.UNAUTHORIZED);
		}

		return courseService.postCourse(loginAuth, dto);
	}


}
