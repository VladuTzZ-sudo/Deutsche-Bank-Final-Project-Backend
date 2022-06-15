package com.app.eLearning.controller;

import com.app.eLearning.dao.User;
import com.app.eLearning.dto.LoginDTO;
import com.app.eLearning.dto.RegisterDTO;
import com.app.eLearning.exceptions.*;
import com.app.eLearning.service.UserService;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws EmailTooShortException, EmailTooLongException, PasswordTooShortException, PasswordTooLongException, UnmatchedLoginCredentials, UserInactiveException {

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

        System.out.println(encodedJWT);

        return ResponseEntity.ok().headers(headers).body("Successful login!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) throws EmailTooLongException, PasswordTooShortException, EmailAlreadyRegisteredException, PasswordTooLongException, EmailTooShortException {

        if (userService.registerUser(registerDTO) == true) {
            return new ResponseEntity("Successfully registered!", HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot register account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
