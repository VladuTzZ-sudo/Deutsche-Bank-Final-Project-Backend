package com.app.eLearning.controller;

import com.app.eLearning.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.ResultSet;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailTooShortException.class)
    public ResponseEntity handleEmailTooShortException() {
        return new ResponseEntity("Email is too short.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailTooLongException.class)
    public ResponseEntity handleEmailTooLongException() {
        return new ResponseEntity("Email is too long.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordTooShortException.class)
    public ResponseEntity handlePasswordTooShortException() {
        return new ResponseEntity("Password is too short.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordTooLongException.class)
    public ResponseEntity handlePasswordTooLongException() {
        return new ResponseEntity("Password is too long.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnmatchedLoginCredentials.class)
    public ResponseEntity handleUnmatchedLoginCredentials() {
        return new ResponseEntity("The email and password do not match.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity handleUserInactiveException() {
        return new ResponseEntity("This account has not been activated.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity handleEmailAlreadyRegisteredException() {
        return new ResponseEntity("This email is already in use", HttpStatus.BAD_REQUEST);
    }

}
