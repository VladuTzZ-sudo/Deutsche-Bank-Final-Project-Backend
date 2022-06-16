package com.app.eLearning.controller;

import com.app.eLearning.dao.User;
import com.app.eLearning.dto.LoginDTO;
import com.app.eLearning.dto.LoginResponseDTO;
import com.app.eLearning.dto.RegisterDTO;
import com.app.eLearning.exceptions.*;
import com.app.eLearning.service.UserService;
import com.app.eLearning.utils.LoginAuthorization;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@CrossOrigin
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public LoginResponseDTO login(@RequestBody LoginDTO loginDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException, UnmatchedLoginCredentials, UserInactiveException {

        Pair<User, String> foundUserAndRole = userService.loginUser(loginDTO);
        User foundUser = foundUserAndRole.getFirst();
        String roleName = foundUserAndRole.getSecond();

        String authorizationStr = foundUser.getId() + ":" + foundUser.getName();

        //fusionauth-jwt
        Signer signer = HMACSigner.newSHA256Signer("my secret key to verify if token is legit ;)");

        JWT jwt = new JWT().setIssuer("www.goaldiggers.com")
                .setIssuedAt(ZonedDateTime.now())
                .setSubject(authorizationStr)
                .setAudience(roleName)
                .setExpiration(ZonedDateTime.now().plusMinutes(1440)); //expira dupa o zi

        String encodedJWT = JWT.getEncoder().encode(jwt, signer);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + encodedJWT);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(encodedJWT, foundUser.getName(), roleName);

        System.out.println(encodedJWT);

        return loginResponseDTO;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) throws EmailTooLongException,
            PasswordTooShortException, EmailAlreadyRegisteredException, PasswordTooLongException,
            EmailTooShortException, InvalidEmailFormatException, NameTooLongException, SurnameTooShortException, NameTooShortException, SurnameTooLongException {

        if (userService.registerUser(registerDTO) == true) {
            return new ResponseEntity("Successfully registered!", HttpStatus.OK);

        } else {
            return new ResponseEntity("Cannot register account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody String token) throws WrongTokenException {

        int a;
        String b;

       a = LoginAuthorization.validateAuthorization(token).getFirst();
        b = LoginAuthorization.validateAuthorization(token).getSecond();

        System.out.println(a);
        System.out.println(b);

        return null;
    }


}
