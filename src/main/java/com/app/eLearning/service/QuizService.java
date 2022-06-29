package com.app.eLearning.service;

import com.app.eLearning.dao.Quiz;
import com.app.eLearning.dao.ReceivedQuizDTO;
import com.app.eLearning.dao.Section;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.repository.QuizRepository;
import com.app.eLearning.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    SectionRepository sectionRepository;

    public Quiz getQuizForSpecificSectionId(int sectionId, String role) throws SectionNotFoundException, QuizNotFoundException {

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

        if (role.equals("teacher")) {
            if (foundSection.getQuiz() != null) {
                return foundSection.getQuiz();
            } else {
                throw new QuizNotFoundException();
            }
        } else {
            if (foundSection.getQuiz() != null) {
                if (foundSection.getQuiz().getIsVisible() == true) {
                    return foundSection.getQuiz();
                } else {
                    throw new QuizNotFoundException();
                }
            } else {
                throw new QuizNotFoundException();
            }
        }


    }

    public ResponseEntity<String> postQuiz(int sectionId, ReceivedQuizDTO receivedQuizDTO) throws SectionNotFoundException {

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


}
