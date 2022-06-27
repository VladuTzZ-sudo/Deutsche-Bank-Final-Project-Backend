package com.app.eLearning.controller;

import com.app.eLearning.dto.TakenQuizResponseDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionIdNotFound;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.TakenQuizService;
import com.app.eLearning.utils.LoginAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class TakenQuizController {

    @Autowired
    TakenQuizService takenQuizService;

    @PostMapping("/quiz/{id}/takenQuiz")
    public ResponseEntity<String> postTakenQuiz(@PathVariable(name = "id")int quizId, @RequestBody List<Integer> answerIdList, @RequestHeader("Authorization") String authHeader) throws WrongTokenException, QuizNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);


        if (!loginAuth.getSecond().equals("student"))
        {
            return new ResponseEntity<>("Teachers cannot take the quiz!", HttpStatus.UNAUTHORIZED);
        }

        if (quizId <= 0){
            return new ResponseEntity<>("QuizId cannot be negative or zero!", HttpStatus.BAD_REQUEST);
        }

        return takenQuizService.postTakenQuiz(loginAuth.getFirst(), answerIdList, quizId);

    }

    @GetMapping("/courses/{courseId}/sections/{sectionId}/quiz/{quizId}/takenQuiz")
    public ResponseEntity<TakenQuizResponseDTO> getTakenQuiz(@PathVariable(name = "courseId")int courseId, @PathVariable(name = "sectionId")int sectionId, @PathVariable(name = "quizId")int quizId, @RequestHeader("Authorization") String authHeader) throws WrongTokenException, SectionIdNotFound, QuizNotFoundException, CourseNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);


        if (!loginAuth.getSecond().equals("student"))
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (quizId <= 0 || courseId <=0 || sectionId <= 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


        return takenQuizService.getTakenQuiz(loginAuth.getFirst(), courseId, sectionId, quizId);


    }
}
