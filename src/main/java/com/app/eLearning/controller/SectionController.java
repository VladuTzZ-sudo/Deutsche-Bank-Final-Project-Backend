package com.app.eLearning.controller;

import com.app.eLearning.dao.Section;
import com.app.eLearning.dto.NewSectionDTO;
import com.app.eLearning.dto.ResponseSectionDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.SectionService;
import com.app.eLearning.service.UserService;
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
public class SectionController {

    @Autowired
    SectionService sectionService;

    @Autowired
    UserService userService;

    @PostMapping("/courses/{id}/sections")
    public ResponseEntity<String> postSection(@PathVariable (name = "id") int courseId, @RequestBody NewSectionDTO newSectionDTO) throws WrongTokenException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(newSectionDTO.getToken());

        if (!loginAuth.getSecond().equals("teacher"))
        {
            return new ResponseEntity<>("You are not authorized to create a new section!", HttpStatus.UNAUTHORIZED);
        }

        if (courseId <= 0){
            return new ResponseEntity<>("Course if cannot be negative or zero!", HttpStatus.BAD_REQUEST);
        }

        return sectionService.postSection(newSectionDTO, courseId);

    }

    @GetMapping("/courses/{id}/sections")
    @ResponseBody
    public List<ResponseSectionDTO> getSectionsForCourse(@PathVariable (name = "id") int courseId, @RequestHeader ("Authorization") String authHeader) throws WrongTokenException, CourseNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!userService.checkIfUserExists(loginAuth.getFirst())) {
            return null;
        }

        if (courseId > 0) {
            return sectionService.getSectionsForCourse(courseId, loginAuth.getSecond());
        } else {
            return null;
        }

    }

    @GetMapping("sections/{id}")
    @ResponseBody
    public Section getSpecificSection(@PathVariable (name = "id") int sectionId, @RequestHeader ("Authorization") String authHeader) throws WrongTokenException, SectionNotFoundException {

        Pair<Integer, String> loginAuth = null;

        loginAuth = LoginAuthorization.validateAuthorization(authHeader);

        if (!userService.checkIfUserExists(loginAuth.getFirst())) {
            return null;
        }

        if (sectionId > 0) {
            return sectionService.getSpecificSection(sectionId, loginAuth.getSecond());
        } else {
            return null;
        }
    }
}
