package com.app.eLearning.controller;

import com.app.eLearning.dto.GivenAnswersDTO;
import com.app.eLearning.dto.TakenQuizResponseDTO;
import com.app.eLearning.exceptions.*;
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

    @PostMapping("/sections/{id}/takenQuiz")
    public ResponseEntity<String> postTakenQuiz(@PathVariable(name = "id")int sectionId, @RequestBody List<GivenAnswersDTO> givenAnswersDTOList, @RequestHeader("Authorization") String authHeader) throws WrongTokenException, QuizNotFoundException, SectionIdNotFound {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);


        if (!loginAuth.getSecond().equals("student"))
        {
            return new ResponseEntity<>("Teachers cannot take the quiz!", HttpStatus.UNAUTHORIZED);
        }

        if (sectionId <= 0){
            return new ResponseEntity<>("Section id cannot be negative or zero!", HttpStatus.BAD_REQUEST);
        }

        return takenQuizService.postTakenQuiz(loginAuth.getFirst(), givenAnswersDTOList, sectionId);

    }

    @GetMapping("/courses/{courseId}/sections/{sectionId}/takenQuiz")
    public ResponseEntity getTakenQuiz(@PathVariable(name = "courseId")int courseId, @PathVariable(name = "sectionId")int sectionId, @RequestHeader("Authorization") String authHeader) throws WrongTokenException, SectionIdNotFound, QuizNotFoundException, CourseNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!loginAuth.getSecond().equals("student"))
        {
            return new ResponseEntity<>("Profesorii nu au dreptul la quiz summary", HttpStatus.UNAUTHORIZED);
        }

        if (courseId <=0 || sectionId <= 0){
            return new ResponseEntity<>("Path variables nu pot fi mai mici sau egale cu zero", HttpStatus.BAD_REQUEST);
        }


        return takenQuizService.getTakenQuiz(loginAuth.getFirst(), courseId, sectionId);


    }

    @GetMapping("/sections/{sectionId}/takenQuiz")
    public ResponseEntity getStartDateTime(@RequestHeader("Authorization") String authHeader, @PathVariable(name = "sectionId")int sectionId) throws WrongTokenException, QuizNotFoundException, SectionIdNotFound {
        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!loginAuth.getSecond().equals("student"))
        {
            return new ResponseEntity<>("Profesorii nu au dreptul la quiz summary", HttpStatus.UNAUTHORIZED);
        }

        if (sectionId <= 0){
            return new ResponseEntity<>("Section id nu poate fi negativ sau zero", HttpStatus.BAD_REQUEST);
        }

        return takenQuizService.getStartDateTime(loginAuth.getFirst(), sectionId);

    }

    @GetMapping("/sections/{sectionId}/takenQuiz/answers")
    public ResponseEntity getAnswersDTO(@RequestHeader("Authorization") String authHeader, @PathVariable(name = "sectionId")int sectionId) throws WrongTokenException, QuizNotFoundException, SectionIdNotFound {
        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!loginAuth.getSecond().equals("student"))
        {
            return new ResponseEntity<>("Profesorii nu au dreptul la quiz summary", HttpStatus.UNAUTHORIZED);
        }

        if (sectionId <= 0){
            return new ResponseEntity<>("Section id nu poate fi negativ sau zero", HttpStatus.BAD_REQUEST);
        }

        return takenQuizService.getAnswersResponseDTO(loginAuth.getFirst(), sectionId);

    }

}
