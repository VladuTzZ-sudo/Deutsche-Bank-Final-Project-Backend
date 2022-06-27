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

            if (answerRepository.findById(e).get().isValidation() == true){
                correctAnswerCounter ++;
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
            throw new QuizNotFoundException();
        }


        TakenQuiz takenQuiz = new TakenQuiz();

        takenQuiz.setQuiz(foundQuiz);
        takenQuiz.setGivenAnswers(givenAnswerList);
        takenQuiz.setGrade((correctAnswerCounter * 100)/takenQuiz.getGivenAnswers().size());

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-mm-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        takenQuiz.setSubmittedDate(currentDate);


        foundUser.addTakenQuiz(takenQuiz);

        userRepository.save(foundUser);

        return new ResponseEntity<>("Answers recorded successfully!", HttpStatus.OK);
    }

    public ResponseEntity<TakenQuizResponseDTO> getTakenQuiz(Integer userId, int courseId, int sectionId, int quizId) throws CourseNotFoundException, SectionIdNotFound, QuizNotFoundException, WrongTokenException {
        User foundUser = null;
        Course foundCourse = null;
        Section foundSection = null;
        Quiz foundQuiz = null;

        try {
            foundCourse = courseRepository.findById(courseId).get();
        }catch (Exception e){
            throw new CourseNotFoundException();
        }

        try {
            foundSection = sectionRepository.findById(sectionId).get();
        }catch (Exception e){
            throw new SectionIdNotFound();
        }

        try {
            foundQuiz = quizRepository.findById(quizId).get();
        }catch (Exception e){
            throw new QuizNotFoundException();
        }

        try {
            foundUser = userRepository.findById(userId).get();
        }catch (Exception e){
            throw new WrongTokenException();
        }

        if (foundUser == null || foundCourse == null || foundSection == null || foundQuiz == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        TakenQuizResponseDTO takenQuizResponseDTO = new TakenQuizResponseDTO();

        takenQuizResponseDTO.setCourseTitle(foundCourse.getName());
        takenQuizResponseDTO.setSectionTitle(foundSection.getTitle());
        takenQuizResponseDTO.setQuizTitle(foundQuiz.getQuizName());
        takenQuizResponseDTO.setDurationQuiz(foundQuiz.getDuration());
        takenQuizResponseDTO.setEndDateQuiz(foundQuiz.getDeadline());
        takenQuizResponseDTO.setDetailsQuiz(foundQuiz.getDescription());

        TakenQuiz foundTakenQuiz = null;

        for (TakenQuiz e: foundUser.getTakenQuizzes()) {
            if (e.getQuiz().getId() == foundQuiz.getId()){
                foundTakenQuiz = e;
                takenQuizResponseDTO.setSubmittedDate(e.getSubmittedDate());
                takenQuizResponseDTO.setQuizMark(e.getGrade());
                break;
            }
        }

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-mm-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(currentDate));


        if (foundQuiz.getDeadline().compareTo(currentDate) > 0){
            takenQuizResponseDTO.setIsQuizEnded(0);
        }else {
            takenQuizResponseDTO.setIsQuizEnded(1);
        }

        return new ResponseEntity<>(takenQuizResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
