package com.app.eLearning.service;

import com.app.eLearning.dao.*;
import com.app.eLearning.dto.QuizDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.QuizRepository;
import com.app.eLearning.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class QuizService
{

	@Autowired
	QuizRepository quizRepository;

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	CourseRepository courseRepository;

	public ResponseEntity<QuizDTO> getQuizDetails(String role, int courseId, int sectionId) throws SectionNotFoundException, QuizNotFoundException, CourseNotFoundException
	{

		Section foundSection = null;
		Course foundCourse = null;

		try
		{
			foundSection = sectionRepository.findFirstById(sectionId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SectionNotFoundException();
		}

		try
		{
			foundCourse = courseRepository.findFirstById(courseId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new CourseNotFoundException();
		}

		if (foundSection == null)
		{
			throw new SectionNotFoundException();
		}

		if (foundCourse == null)
		{
			throw new CourseNotFoundException();
		}

		QuizDTO quizDTO = new QuizDTO();
		if (role.equals("teacher"))
		{
			if (foundSection.getQuiz() != null)
			{
				quizDTO.setQuizTitle(foundSection.getQuiz().getQuizName());
				quizDTO.setDetails(foundSection.getQuiz().getDescription());
				quizDTO.setDuration(foundSection.getQuiz().getDuration());
				quizDTO.setEndDate(foundSection.getQuiz().getDeadline());
				quizDTO.setSubjectTitle(foundCourse.getName());
				quizDTO.setSectionTitle(foundSection.getTitle());
				return new ResponseEntity<>(quizDTO, HttpStatus.OK);
			}
			else
				throw new QuizNotFoundException();
		}
		else
		{
			if (foundSection.getQuiz() != null)
			{
				if (foundSection.getQuiz().getIsVisible())
				{
					quizDTO.setQuizTitle(foundSection.getQuiz().getQuizName());
					quizDTO.setDetails(foundSection.getQuiz().getDescription());
					quizDTO.setDuration(foundSection.getQuiz().getDuration());
					quizDTO.setEndDate(foundSection.getQuiz().getDeadline());
					quizDTO.setSubjectTitle(foundCourse.getName());
					quizDTO.setSectionTitle(foundSection.getTitle());
					return new ResponseEntity<>(quizDTO, HttpStatus.OK);
				}
				else
					throw new QuizNotFoundException();
			}
			else
				throw new QuizNotFoundException();
		}
	}


	public ResponseEntity<Set<Question>> getQuestionsAndAnswers(String role, int courseId, int sectionId) throws SectionNotFoundException, QuizNotFoundException
	{
		Section foundSection = null;

		try
		{
			foundSection = sectionRepository.findFirstById(sectionId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SectionNotFoundException();
		}
		if (foundSection == null)
		{
			throw new SectionNotFoundException();
		}


		if (foundSection.getQuiz() != null)
		{
			if(role.equals("student") && !foundSection.getQuiz().getIsVisible())
			{
				throw new QuizNotFoundException();
			}
			return new ResponseEntity<>(foundSection.getQuiz().getQuestions(), HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> postQuiz(int sectionId, Quiz quiz) throws SectionNotFoundException
	{

		//check if QuizContentDTO has null values
		if (quiz.getQuizName() == null || quiz.getDescription() == null ||
				quiz.getDeadline() == null)
		{
			return new ResponseEntity<>("Fields of QuizContentDTO cannot be null", HttpStatus.UNAUTHORIZED);
		}

		Section foundSection = null;

		try
		{
			foundSection = sectionRepository.findFirstById(sectionId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SectionNotFoundException();
		}

		if (foundSection == null)
		{
			throw new SectionNotFoundException();
		}

		try
		{
			foundSection.setQuiz(quiz);
			sectionRepository.saveAndFlush(foundSection);
			return new ResponseEntity<>("Quiz inserted!", HttpStatus.OK);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>("Could not insert new Quiz", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
