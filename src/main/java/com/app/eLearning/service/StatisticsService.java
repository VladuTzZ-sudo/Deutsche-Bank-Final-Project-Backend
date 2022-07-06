package com.app.eLearning.service;

import com.app.eLearning.dao.*;
import com.app.eLearning.dto.*;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.repository.*;
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

    @Autowired
    UserRoleRepository userRoleRepository;

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

        List<UserRole> userRoleList = new ArrayList<>();

        try {
            userRoleList = userRoleRepository.findAll();
        } catch (Exception e) {
            return new ResponseEntity<>("Could not find user_roles", HttpStatus.NOT_FOUND);
        }

        for (User user : userList) {
            boolean isStudent = false;
//            for (UserRole userRole : userRoleList){
//                if (userRole.getId() == user.getUserRole().getId()){
//                    if ()
//                }
//            }

            if (user.getUserRole().getRoleId() == 2 && user.getActive() != null) {
                float totalPoints = 0;
                for (TakenQuiz takenQuiz : user.getTakenQuizzes()) {
                    totalPoints += takenQuiz.getGrade();
                }
                leaderboardDTOList.add(new LeaderboardDTO(user.getName() + " " + user.getSurname(), totalPoints));

            }
        }


        Collections.sort(leaderboardDTOList, Collections.reverseOrder());

        List<LeaderboardDTO> limitedInSizeList = new ArrayList<>();
        if (leaderboardDTOList.size() < 10) {
            return new ResponseEntity<>(leaderboardDTOList, HttpStatus.OK);
        } else {
            int counter = 0;

            for (LeaderboardDTO leaderboardDTO : leaderboardDTOList) {
                limitedInSizeList.add(leaderboardDTO);
                if (counter == 10) {
                    break;
                } else {
                    counter++;
                }
            }
        }

        return new ResponseEntity<>(limitedInSizeList, HttpStatus.OK);
    }

    public ResponseEntity getResources() throws CourseNotFoundException {

        List<Course> foundCourseList = new ArrayList<>();
        List<User> foundUserList = new ArrayList<>();

        try {
            foundCourseList = courseRepository.findAll();
        } catch (Exception e) {
            throw new CourseNotFoundException();
        }

        if (foundCourseList.size() <= 0) {
            throw new CourseNotFoundException();
        }

        try {
            foundUserList = userRepository.findAll();
        } catch (Exception e) {
            return null;
        }

        if (foundCourseList.size() <= 0) {
            return null;
        }

        List<RCourseDTO> resourceList = new ArrayList<>();

        for (Course c : foundCourseList) {
            RCourseDTO rCourseDTO = new RCourseDTO();

            rCourseDTO.setName(c.getName());
            rCourseDTO.setTeacherName(c.getTeacherName());
            rCourseDTO.setDescription(c.getDescription());

            List<RSectionDTO> rSectionDTOList = new ArrayList<>();

            for (Section s : c.getCourseSections()) {
                if (s.getQuiz() == null) {
                    break;
                }

                RSectionDTO rSectionDTO = new RSectionDTO();

                if (s.getQuiz() != null) {
                    rSectionDTO.setDescription(s.getDescription());
                    rSectionDTO.setQuizName(s.getQuiz().getQuizName());
                    rSectionDTO.setQuizDescription(s.getQuiz().getDescription());
                    rSectionDTO.setTitle(s.getTitle());

                    List<RContentDTO> rContentDTOList = new ArrayList<>();
                    for (User u : foundUserList) {
                        RContentDTO rContentDTO = new RContentDTO();
                        boolean userHasTakenQuiz = false;
                        for (TakenQuiz q : u.getTakenQuizzes()) {
                            if (q.getQuiz().getId() == s.getQuiz().getId()) {
                                rContentDTO.setName(u.getName());
                                rContentDTO.setSurname(u.getSurname());
                                rContentDTO.setGrade(q.getGrade());
                                userHasTakenQuiz = true;
                                break;
                            }
                        }
                        if (userHasTakenQuiz) {
                            rContentDTOList.add(rContentDTO);
                        }

                    }
                    rSectionDTO.setRContentDTOList(rContentDTOList);
                }

                rSectionDTOList.add(rSectionDTO);
            }
            rCourseDTO.setRSectionDTOList(rSectionDTOList);
            resourceList.add(rCourseDTO);
        }


        return new ResponseEntity<>(resourceList, HttpStatus.OK);

    }
}
