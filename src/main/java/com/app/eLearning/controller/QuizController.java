package com.app.eLearning.controller;

import com.app.eLearning.dao.Quiz;
import com.app.eLearning.dto.NewQuizDTO;
import com.app.eLearning.dto.ReceivedSectionDTO;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionIdNotFound;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.QuizService;
import com.app.eLearning.service.UserService;
import com.app.eLearning.utils.LoginAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
public class QuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    UserService userService;

    @GetMapping("/sections/{id}/quiz")
    public ResponseEntity<String> getQuiz(@PathVariable(name = "id")int sectionId, @RequestHeader ("Authorization") String authHeader) throws QuizNotFoundException, SectionNotFoundException, SectionIdNotFound, WrongTokenException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);


        if (!userService.checkIfUserExists(loginAuth.getFirst())) {
            return new ResponseEntity<>("Access forbidden! User account doesn't exist or is inactive.", HttpStatus.UNAUTHORIZED);
        }

        if (sectionId > 0) {
            return new ResponseEntity(quizService.getQuizForSpecificSectionId(sectionId, loginAuth.getSecond()), HttpStatus.OK);
        } else {
            return new ResponseEntity("Section id cannot be negatice or zero!", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/sections/{id}/quiz")
    public ResponseEntity<String> postQuiz(@PathVariable(name = "id")int sectionId, @RequestBody NewQuizDTO newQuizDTO) throws SectionNotFoundException, WrongTokenException {
        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(newQuizDTO.getToken());


        if (!loginAuth.getSecond().equals("teacher"))
        {
            return new ResponseEntity<>("You are not authorized to create a new quiz!", HttpStatus.UNAUTHORIZED);
        }

        if (sectionId > 0){
            return quizService.postQuiz(sectionId, newQuizDTO);
        }else {
            return new ResponseEntity("Section id cannot be negatice or zero!", HttpStatus.BAD_REQUEST);
        }
    }

}
