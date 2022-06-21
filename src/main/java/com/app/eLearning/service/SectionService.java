package com.app.eLearning.service;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dao.Section;
import com.app.eLearning.dto.NewSectionDTO;
import com.app.eLearning.dto.ResponseQuizDTO;
import com.app.eLearning.dto.ResponseSectionDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    CourseRepository courseRepository;

    public ResponseEntity<String> postSection(NewSectionDTO newSectionDTO, int courseID) {

        Course foundCourse = null;

        try {
            foundCourse = courseRepository.findById(courseID).get();
        } catch (Exception e) {
            return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST);
        }

        if (foundCourse == null) {
            return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST);
        }

        try {
            foundCourse.addSection(newSectionDTO.getSection());
            courseRepository.saveAndFlush(foundCourse);
            return new ResponseEntity<>("Section inserted!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Could not insert new Section", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ResponseSectionDTO> getSectionsForCourse(int courseId, String role) throws CourseNotFoundException {
        Course foundCourse = null;

        List<ResponseSectionDTO> sectionListDTO = new ArrayList<>();

        try {
            foundCourse = courseRepository.findById(courseId).get();
        } catch (Exception e) {
            throw new CourseNotFoundException();
        }

        if (foundCourse == null) {
            throw new CourseNotFoundException();
        }

        if (role.equals("teacher")) {
            for (Section s : foundCourse.getCourseSections()) {
                if (s.getQuiz() == null) {
                    sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), null));
                } else {
                    sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), new ResponseQuizDTO(s.getQuiz().getId(), s.getQuiz().getQuizName(), s.getQuiz().getDescription(), s.getQuiz().getIsVisible())));
                }
            }
            return sectionListDTO;
        } else {
            for (Section s : foundCourse.getCourseSections()) {
                if (s.getQuiz() == null) {
                    sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), null));
                } else if (s.getQuiz().getIsVisible() == true) {
                    sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), new ResponseQuizDTO(s.getQuiz().getId(), s.getQuiz().getQuizName(), s.getQuiz().getDescription(), s.getQuiz().getIsVisible())));
                } else if (s.getQuiz().getIsVisible() == false) {
                    sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), null));
                }
            }
            return sectionListDTO;
        }
    }

}
