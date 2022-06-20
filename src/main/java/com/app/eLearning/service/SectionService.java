package com.app.eLearning.service;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dto.NewSectionDTO;
import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SectionService {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    CourseRepository courseRepository;

    public ResponseEntity<String> postSection(NewSectionDTO newSectionDTO, int courseID){

        Course foundCourse = null;

        try {
            foundCourse = courseRepository.findById(courseID).get();
        }catch (Exception e){
            return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST);
        }

        if (foundCourse == null){
            return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST);
        }

        try {
            foundCourse.addSection(newSectionDTO.getSection());
            courseRepository.saveAndFlush(foundCourse);
            return new ResponseEntity<>("Section inserted!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Could not insert new Section", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
