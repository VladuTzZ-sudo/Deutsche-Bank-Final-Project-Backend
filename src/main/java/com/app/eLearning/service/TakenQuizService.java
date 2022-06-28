package com.app.eLearning.service;

import com.app.eLearning.dao.*;
import com.app.eLearning.dto.TakenQuizResponseDTO;
import com.app.eLearning.exceptions.*;
import com.app.eLearning.repository.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class TakenQuizService {

    @Autowired
    TakenQuizRepository takenQuizRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    public ResponseEntity<String> postTakenQuiz(Integer userId, List<Integer> answerIdList, int quizId) throws WrongTokenException, QuizNotFoundException {

        User foundUser = userRepository.findById(userId).get();
        Quiz foundQuiz = null;
        int correctAnswerCounter = 0;


        Set<GivenAnswer> givenAnswerList = new HashSet<>();
        for (int e : answerIdList) {
            givenAnswerList.add(new GivenAnswer(e));

            if (answerRepository.findById(e).get().isValidation() == true) {
                correctAnswerCounter++;
            }
        }

        try {
            foundQuiz = quizRepository.findById(quizId).get();
        } catch (Exception e) {
            throw new QuizNotFoundException();
        }

        if (foundUser == null) {
            throw new WrongTokenException();
        }

        if (foundQuiz == null || foundQuiz.getIsVisible() == false) {
            return new ResponseEntity<>("Quiz-ul nu exista sau nu este activ!", HttpStatus.BAD_REQUEST);
        }

        TakenQuiz takenQuiz = null;

        for (TakenQuiz e : foundUser.getTakenQuizzes()) {
            if (e.getQuiz().getId() == foundQuiz.getId()) {
                takenQuiz = e;
                break;
            }
        }

        if (takenQuiz == null) {
            return new ResponseEntity<>("Taken quiz nu exista, ai uitat sa faci GET de start time la inceputul quiz-ului???", HttpStatus.BAD_REQUEST);
        }

        if (takenQuiz.getGivenAnswers().size() > 0){
            return new ResponseEntity<>("Answers have already been given to this quiz from this user", HttpStatus.BAD_REQUEST);
        }

        takenQuiz.setGivenAnswers(givenAnswerList);
        takenQuiz.setGrade((correctAnswerCounter * 100) / takenQuiz.getGivenAnswers().size());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date currentDate = new Date(System.currentTimeMillis());

        takenQuiz.setSubmittedDate(currentDate);

        takenQuizRepository.save(takenQuiz);

        return new ResponseEntity<>("Answers recorded successfully!", HttpStatus.OK);
    }

    public ResponseEntity getTakenQuiz(Integer userId, int courseId, int sectionId, int quizId) throws CourseNotFoundException, SectionIdNotFound, QuizNotFoundException, WrongTokenException {
        User foundUser = null;
        Course foundCourse = null;
        Section foundSection = null;
        Quiz foundQuiz = null;

        try {
            foundCourse = courseRepository.findById(courseId).get();
        } catch (Exception e) {
            throw new CourseNotFoundException();
        }

        try {
            foundSection = sectionRepository.findById(sectionId).get();
        } catch (Exception e) {
            throw new SectionIdNotFound();
        }

        try {
            foundQuiz = quizRepository.findById(quizId).get();
        } catch (Exception e) {
            throw new QuizNotFoundException();
        }

        try {
            foundUser = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new WrongTokenException();
        }

        if (foundUser == null || foundCourse == null || foundSection == null || foundQuiz == null) {
            return new ResponseEntity<>("Nu a fost gasit un obiect: user, course, section sau quiz", HttpStatus.BAD_REQUEST);
        }

        TakenQuizResponseDTO takenQuizResponseDTO = new TakenQuizResponseDTO();

        takenQuizResponseDTO.setCourseTitle(foundCourse.getName());
        takenQuizResponseDTO.setSectionTitle(foundSection.getTitle());
        takenQuizResponseDTO.setQuizTitle(foundQuiz.getQuizName());
        takenQuizResponseDTO.setDurationQuiz(foundQuiz.getDuration());
        takenQuizResponseDTO.setEndDateQuiz(foundQuiz.getDeadline());
        takenQuizResponseDTO.setDetailsQuiz(foundQuiz.getDescription());

        TakenQuiz foundTakenQuiz = null;

        for (TakenQuiz e : foundUser.getTakenQuizzes()) {
            if (e.getQuiz().getId() == foundQuiz.getId()) {
                foundTakenQuiz = e;
                takenQuizResponseDTO.setSubmittedDate(e.getSubmittedDate());
                takenQuizResponseDTO.setQuizMark(e.getGrade());
                break;
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(currentDate));


        if (foundQuiz.getDeadline().compareTo(currentDate) > 0) {
            takenQuizResponseDTO.setIsQuizEnded(0);
        } else {
            takenQuizResponseDTO.setIsQuizEnded(1);
        }

        return new ResponseEntity<>(takenQuizResponseDTO, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity getStartDateTime(Integer userId, int quizId) throws WrongTokenException, QuizNotFoundException {

        Quiz foundQuiz = null;
        User foundUser = null;

        try {
            foundUser = userRepository.findById(userId).get();
        } catch (Exception e) {
            throw new WrongTokenException();
        }

        try {
            foundQuiz = quizRepository.findById(quizId).get();
        } catch (Exception e) {
            throw new QuizNotFoundException();
        }

        if (foundUser == null) {
            return new ResponseEntity<>("Utilizatorul nu poate fi identificat", HttpStatus.BAD_REQUEST);
        }

        if (foundQuiz == null || foundQuiz.getIsVisible() == false) {
            return new ResponseEntity<>("Quiz-ul nu exista sau nu este activ!", HttpStatus.BAD_REQUEST);
        }

        for (TakenQuiz e : foundUser.getTakenQuizzes()) {
            if (e.getQuiz().getId() == foundQuiz.getId()) {
                return new ResponseEntity<>(e.getStartDateTime(), HttpStatus.BAD_REQUEST);
            }
        }

        TakenQuiz takenQuiz = new TakenQuiz();
        takenQuiz.setStartDateTime(new Date(System.currentTimeMillis()));
        takenQuiz.setQuiz(foundQuiz);

        foundUser.addTakenQuiz(takenQuiz);
        userRepository.save(foundUser);

        return new ResponseEntity<>(takenQuiz.getStartDateTime(), HttpStatus.BAD_REQUEST);
    }
}
