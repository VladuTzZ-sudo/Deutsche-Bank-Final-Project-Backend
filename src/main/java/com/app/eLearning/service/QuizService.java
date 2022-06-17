package com.app.eLearning.service;

import com.app.eLearning.dao.Quiz;
import com.app.eLearning.dao.Section;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.repository.QuizRepository;
import com.app.eLearning.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    SectionRepository sectionRepository;

    public Quiz getQuizForSpecificSectionId(int sectionId) throws SectionNotFoundException, QuizNotFoundException {

        Section foundSection = null;

        try {
            foundSection = sectionRepository.findFirstById(sectionId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SectionNotFoundException();
        }

        if (foundSection == null){
            throw new SectionNotFoundException();
        }

        if (foundSection != null && foundSection.getQuiz() != null) {
            return foundSection.getQuiz();
        } else {
            throw new QuizNotFoundException();
        }

    }

}
