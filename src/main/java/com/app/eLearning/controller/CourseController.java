package com.app.eLearning.controller;

import com.app.eLearning.dto.CourseResponseDTO;
import com.app.eLearning.dto.CreateCourseDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.CourseService;
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
public class CourseController
{
	@Autowired
	CourseService courseService;

	@GetMapping("/courses")
	@ResponseBody
	public List<CourseResponseDTO> getAllCourses(@RequestBody String token)
	{
		Pair<Integer, String> loginAuth = null;
		try
		{
			loginAuth = LoginAuthorization.validateAuthorization(token);
		}
		catch (WrongTokenException e)
		{
			e.printStackTrace();
		}

		return courseService.getAllCourses(loginAuth);
	}

	@GetMapping("/courses/{id}")
	@ResponseBody
	public CourseResponseDTO getCourse(@PathVariable(required = true, name = "id") Integer id,
	                                   @RequestBody String token) throws CourseNotFoundException
	{
		Pair<Integer, String> loginAuth = null;
		try
		{
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
	public ResponseEntity<String> postCourse(@RequestBody CreateCourseDTO dto)
	{
		Pair<Integer, String> loginAuth = null;
		try
		{
			loginAuth = LoginAuthorization.validateAuthorization(dto.getToken());
		}
		catch (WrongTokenException e)
		{
			e.printStackTrace();
		}

		assert loginAuth != null;
		if (!loginAuth.getSecond().equals("teacher"))
		{
			return new ResponseEntity<>("You are not authorized to create a new course!", HttpStatus.UNAUTHORIZED);
		}

		return courseService.postCourse(loginAuth, dto);
	}


}
