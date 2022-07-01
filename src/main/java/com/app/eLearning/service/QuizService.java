package com.app.eLearning.service;

import com.app.eLearning.dao.Quiz;
import com.app.eLearning.dao.ReceivedQuizDTO;
import com.app.eLearning.dao.Section;
import com.app.eLearning.dao.*;
import com.app.eLearning.dto.QuizAndAnswersDTO;
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

import java.util.*;

@Service
public class QuizService {

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
				quizDTO.setEndDate(foundSection.getQuiz().getDeadline().getTime());
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
					quizDTO.setEndDate(foundSection.getQuiz().getDeadline().getTime());
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


	public ResponseEntity getQuestionsAndAnswers(String role, int courseId, int sectionId) throws SectionNotFoundException, QuizNotFoundException
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
			if (role.equals("student") && !foundSection.getQuiz().getIsVisible())
			{
				throw new QuizNotFoundException();
			}
			return new ResponseEntity<>(foundSection.getQuiz().getQuestions(), HttpStatus.OK);
		}
		else
			throw new QuizNotFoundException();
	}

    public ResponseEntity<String> postQuiz(int sectionId, AdaptedReceivedQuizDTO adaptedReceivedQuizDTO) throws SectionNotFoundException {
		//converting AdaptedReceivedQuizDTO to ReceivedQuizDTO
		ReceivedQuizDTO receivedQuizDTO = AdaptingReceivedQuizDTO(adaptedReceivedQuizDTO);


        //check if QuizContentDTO has null values
        if (receivedQuizDTO.getQuizName() == null || receivedQuizDTO.getDescription() == null ||
                receivedQuizDTO.getDeadline() == null) {
            return new ResponseEntity<>("Fields of Quiz cannot be null", HttpStatus.UNAUTHORIZED);
        }

        Section foundSection = null;

        try {
            foundSection = sectionRepository.findFirstById(sectionId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SectionNotFoundException();
        }

        if (foundSection == null) {
            throw new SectionNotFoundException();
        }

        try{
            Quiz quiz = new Quiz();
            quiz.setQuizName(receivedQuizDTO.getQuizName());
            quiz.setDescription(receivedQuizDTO.getDescription());
            quiz.setQuestions(receivedQuizDTO.getQuestions());
            quiz.setIsVisible(receivedQuizDTO.getIsVisible());
            quiz.setDuration(receivedQuizDTO.getDuration());
            quiz.setDeadline(new Date(receivedQuizDTO.getDeadline()));

            foundSection.setQuiz(quiz);
            sectionRepository.saveAndFlush(foundSection);
            return new ResponseEntity<>("Quiz inserted!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Could not insert new Quiz", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

	private ReceivedQuizDTO AdaptingReceivedQuizDTO(AdaptedReceivedQuizDTO adaptedReceivedQuizDTO){
		ReceivedQuizDTO receivedQuizDTO = new ReceivedQuizDTO();
		receivedQuizDTO.setQuizName(adaptedReceivedQuizDTO.getQuizzTitle());
		receivedQuizDTO.setDescription(adaptedReceivedQuizDTO.getDetails());
		receivedQuizDTO.setIsVisible(false);
		receivedQuizDTO.setDeadline(adaptedReceivedQuizDTO.getDue());
		receivedQuizDTO.setDuration(adaptedReceivedQuizDTO.getDuration());
		//set questions
		Set<Question> questionList = new HashSet<>();
		for (AdaptedQuestionsDTO e : adaptedReceivedQuizDTO.getQuestions()){
			Set<Answer> answerList = new HashSet<>();
			for (AdaptedAnswersDTO adaptedAnswersDTO : e.getAnswers()){
				Answer answer = new Answer();
				answer.setAnswerContent(adaptedAnswersDTO.getAnswerText());
				if (adaptedAnswersDTO.getAnswerValue().equals("true")){
					answer.setValidation(true);
				}else{
					answer.setValidation(false);
				}
				answerList.add(answer);
			}
			Question question = new Question();
			question.setContentQuestion(e.getQuestionText());
			question.setAnswers(answerList);

			questionList.add(question);
		}

		receivedQuizDTO.setQuestions(questionList);

		return receivedQuizDTO;
	}


	public ResponseEntity getQuizFromSection(int sectionId) throws SectionNotFoundException, QuizNotFoundException {

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
			QuizAndAnswersDTO quizAndAnswersDTO = new QuizAndAnswersDTO();

			quizAndAnswersDTO.setId(foundSection.getQuiz().getId());
			quizAndAnswersDTO.setQuizName(foundSection.getQuiz().getQuizName());
			quizAndAnswersDTO.setDescription(foundSection.getQuiz().getDescription());
			quizAndAnswersDTO.setIsVisible(foundSection.getQuiz().getIsVisible());
			quizAndAnswersDTO.setDeadline(foundSection.getQuiz().getDeadline().getTime());
			quizAndAnswersDTO.setDuration(foundSection.getQuiz().getDuration());
			quizAndAnswersDTO.setQuestions(foundSection.getQuiz().getQuestions());
			
			return new ResponseEntity<>(quizAndAnswersDTO, HttpStatus.OK);
		}
		else
			throw new QuizNotFoundException();

	}
}
