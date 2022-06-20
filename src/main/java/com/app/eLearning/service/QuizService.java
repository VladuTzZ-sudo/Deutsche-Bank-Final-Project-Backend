package com.app.eLearning.service;

import com.app.eLearning.dao.Quiz;
import com.app.eLearning.dao.Section;
import com.app.eLearning.dto.NewQuizDTO;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.repository.QuizRepository;
import com.app.eLearning.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<String> postQuiz(NewQuizDTO newQuizDTO) throws SectionNotFoundException {

        //check if QuizContentDTO has null values
        if (newQuizDTO.getQuiz().getQuizName() == null || newQuizDTO.getQuiz().getDescription() == null ||
                newQuizDTO.getQuiz().getDeadline() == null) {
            return new ResponseEntity<>("Fields of QuizContentDTO cannot be null", HttpStatus.UNAUTHORIZED);
        }
        //TODO: add additional validations

        Section foundSection = null;

        try {
            foundSection = sectionRepository.findFirstById(newQuizDTO.getSectionID());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SectionNotFoundException();
        }

        if (foundSection == null) {
            throw new SectionNotFoundException();
        }

        try{
            foundSection.setQuiz(newQuizDTO.getQuiz());
            sectionRepository.saveAndFlush(foundSection);
            return new ResponseEntity<>("Quiz inserted!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Could not insert new Quiz", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
