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
            return new ResponseEntity<>("You are not authorized view statistics!", HttpStatus.UNAUTHORIZED);
        }

        return statisticsService.getListofPopularCourseDTO();


    }

}
