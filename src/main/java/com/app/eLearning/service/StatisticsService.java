package com.app.eLearning.service;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dao.Section;
import com.app.eLearning.dao.TakenQuiz;
import com.app.eLearning.dao.User;
import com.app.eLearning.dto.LeaderboardDTO;
import com.app.eLearning.dto.PopularCourseDTO;
import com.app.eLearning.dto.SectionAverageGradeDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.SectionRepository;
import com.app.eLearning.repository.TakenQuizRepository;
import com.app.eLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    TakenQuizRepository takenQuizRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity getListofPopularCourseDTO() throws CourseNotFoundException {

        List<Course> courseList = new ArrayList<>();
        List<TakenQuiz> takenQuizList = new ArrayList<>();
        List<PopularCourseDTO> popularCourseDTOList = new ArrayList<>();

        try {
            courseList = courseRepository.findAll();
        } catch (Exception e) {
            throw new CourseNotFoundException();
        }

        if (courseList.size() < 1) {
            throw new CourseNotFoundException();
        }

        try {
            takenQuizList = takenQuizRepository.findAll();
        } catch (Exception e) {
            return new ResponseEntity("No taken quizzes found", HttpStatus.NOT_FOUND);
        }

        if (takenQuizList.size() < 1) {
            return new ResponseEntity("No taken quizzes found", HttpStatus.NOT_FOUND);
        }


        for (Course c : courseList) {
            List<Section> sectionList = c.getCourseSections();
            List<Integer> allQuizzIdsForACourse = new ArrayList<>();

            for (Section s : sectionList) {
                if (s.getQuiz() != null) {
                    allQuizzIdsForACourse.add(s.getQuiz().getId());
                }
            }

            PopularCourseDTO popularCourseDTO = new PopularCourseDTO();
            popularCourseDTO.setCourseName(c.getName());

            for (TakenQuiz takenQuiz : takenQuizList) {
//                if (allQuizzIdsForACourse.contains(takenQuiz.getQuiz().getId())){
//                    popularCourseDTO.increaseCoursePopulairty();
//                }
                for (int quizId : allQuizzIdsForACourse) {
                    if (quizId == takenQuiz.getQuiz().getId()) {
                        popularCourseDTO.increaseCoursePopulairty();
                    }
                }
            }

            popularCourseDTO.setCourseId(c.getId());
            popularCourseDTOList.add(popularCourseDTO);
        }

        return new ResponseEntity(popularCourseDTOList, HttpStatus.OK);

    }

    public ResponseEntity getListOfSectionAverageGrade(int courseId) throws CourseNotFoundException {

        Course foundCourse = null;
        List<TakenQuiz> takenQuizList = takenQuizRepository.findAll();
        List<SectionAverageGradeDTO> sectionAverageGradeDTOList = new ArrayList<>();

        try {
            foundCourse = courseRepository.findById(courseId).get();
        } catch (Exception e) {
            throw new CourseNotFoundException();
        }


        for (Section s : foundCourse.getCourseSections()) {
            float sum = 0;
            int count = 0;
            if (s.getQuiz() != null) {
                for (TakenQuiz takenQuiz : takenQuizList) {
                    if (s.getQuiz().getId() == takenQuiz.getQuiz().getId()) {
                        sum += takenQuiz.getGrade();
                        count++;
                    }
                }
            }

            SectionAverageGradeDTO sectionAverageGradeDTO = new SectionAverageGradeDTO();
            if (count != 0) {
                sectionAverageGradeDTO.setAverageGrade(sum / count);
            }
            sectionAverageGradeDTO.setSectionName(s.getTitle());

            sectionAverageGradeDTOList.add(sectionAverageGradeDTO);
        }


        return new ResponseEntity(sectionAverageGradeDTOList, HttpStatus.OK);


    }

    public ResponseEntity getLeaderboard() {

        List<User> userList = new ArrayList<>();
        List<LeaderboardDTO> leaderboardDTOList = new ArrayList<>();

        try {
            userList = userRepository.findAll();
        } catch (Exception e) {
            return new ResponseEntity<>("Could not find users!", HttpStatus.NOT_FOUND);
        }

        if (userList.size() <= 0) {
            return new ResponseEntity<>("Could not find users!", HttpStatus.NOT_FOUND);
        }

        for (User user : userList) {
            float totalPoints = 0;
            for (TakenQuiz takenQuiz : user.getTakenQuizzes()) {
                totalPoints += takenQuiz.getGrade();
            }
            leaderboardDTOList.add(new LeaderboardDTO(user.getName() + " " + user.getSurname(), totalPoints));
        }


       Collections.sort(leaderboardDTOList, Collections.reverseOrder());

        List<LeaderboardDTO> limitedInSizeList = new ArrayList<>();
        if (leaderboardDTOList.size() < 10){
            return new ResponseEntity<>(leaderboardDTOList, HttpStatus.OK);
        }else {
            int counter = 0;

            for (LeaderboardDTO leaderboardDTO : leaderboardDTOList){
                limitedInSizeList.add(leaderboardDTO);
                if (counter == 10){
                    break;
                }else {
                    counter ++;
                }
            }
        }

        return new ResponseEntity<>(limitedInSizeList, HttpStatus.OK);
    }
}
