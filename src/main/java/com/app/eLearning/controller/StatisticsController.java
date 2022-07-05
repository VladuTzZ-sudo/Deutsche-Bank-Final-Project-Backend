package com.app.eLearning.controller;

import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.StatisticsService;
import com.app.eLearning.utils.LoginAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@CrossOrigin
@Controller
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/statistics/popularCourses")
    public ResponseEntity getListOfCoursesAndTakenQuizCount(@RequestHeader("Authorization") String authHeader) throws WrongTokenException, CourseNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!loginAuth.getSecond().equals("teacher"))
        {
            return new ResponseEntity<>("You are not authorized to view statistics!", HttpStatus.UNAUTHORIZED);
        }

        return statisticsService.getListofPopularCourseDTO();


    }

    @GetMapping("statistics/courses/{id}/averageGrade")
    public ResponseEntity getListOfAverageGradePerSection(@RequestHeader("Authorization") String authHeader, @PathVariable(name = "id")int courseId) throws WrongTokenException, CourseNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!loginAuth.getSecond().equals("teacher"))
        {
            return new ResponseEntity<>("You are not authorized to view statistics!", HttpStatus.UNAUTHORIZED);
        }

        if (courseId <= 0){
            return new ResponseEntity<>("Course id cannot be negative or zero!", HttpStatus.BAD_REQUEST);
        }

        return statisticsService.getListOfSectionAverageGrade(courseId);

    }

    @GetMapping("/statistics/leaderboard")
    public ResponseEntity getLeaderboard(@RequestHeader("Authorization") String authHeader) throws WrongTokenException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        return statisticsService.getLeaderboard();

    }

    @GetMapping("statistics/resources")
    public ResponseEntity getResources(@RequestHeader("Authorization") String authHeader) throws WrongTokenException, CourseNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!loginAuth.getSecond().equals("teacher"))
        {
            return new ResponseEntity<>("You are not authorized to view statistics!", HttpStatus.UNAUTHORIZED);
        }

        return statisticsService.getResources();



    }







}
