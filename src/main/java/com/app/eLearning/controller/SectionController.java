package com.app.eLearning.controller;

import com.app.eLearning.dto.NewSectionDTO;
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

@CrossOrigin
@Controller
public class SectionController {

    @Autowired
    SectionService sectionService;

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
}
