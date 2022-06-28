package com.app.eLearning.controller;

import com.app.eLearning.dao.Question;
import com.app.eLearning.dao.Quiz;
import com.app.eLearning.dto.QuizDTO;
import com.app.eLearning.exceptions.*;
import com.app.eLearning.service.QuizService;
import com.app.eLearning.service.UserService;
import com.app.eLearning.utils.LoginAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin
@Controller
public class QuizController
{

	@Autowired
	QuizService quizService;

	@Autowired
	UserService userService;

	@GetMapping("/courses/{courseId}/sections/{sectionId}/quizPlay")
	public ResponseEntity<Set<Question>> getQuestionsAndAnswers(@PathVariable(name = "courseId") int courseId, @PathVariable(name = "sectionId") int sectionId,
	                                                            @RequestHeader("Authorization") String authHeader) throws WrongTokenException, SectionNotFoundException, QuizNotFoundException
	{
		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		if (!userService.checkIfUserExists(loginAuth.getFirst()))
		{
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}

		if (sectionId > 0)
		{
			return quizService.getQuestionsAndAnswers(loginAuth.getSecond(), courseId, sectionId);
		}
		else
		{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/courses/{courseId}/sections/{sectionId}/quizStart")
	public ResponseEntity<QuizDTO> getQuiz(@PathVariable(name = "courseId") int courseId, @PathVariable(name = "sectionId") int sectionId,
	                                       @RequestHeader("Authorization") String authHeader) throws QuizNotFoundException, SectionNotFoundException, WrongTokenException, CourseNotFoundException
	{

		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		if (!userService.checkIfUserExists(loginAuth.getFirst()))
		{
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}

		if (sectionId > 0)
		{
			return quizService.getQuizDetails(loginAuth.getSecond(), courseId, sectionId);
		}
		else
		{
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping("/sections/{id}/quiz")
	public ResponseEntity<String> postQuiz(@PathVariable(name = "id") int sectionId, @RequestBody Quiz quiz, @RequestHeader("Authorization") String authHeader) throws SectionNotFoundException, WrongTokenException
	{
		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);


		if (!loginAuth.getSecond().equals("teacher"))
		{
			return new ResponseEntity<>("You are not authorized to create a new quiz!", HttpStatus.UNAUTHORIZED);
		}

		if (sectionId > 0)
		{
			return quizService.postQuiz(sectionId, quiz);
		}
		else
		{
			return new ResponseEntity("Section id cannot be negatie or zero!", HttpStatus.BAD_REQUEST);
		}
	}

}
