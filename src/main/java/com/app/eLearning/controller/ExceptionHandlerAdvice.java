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

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity handleInvalidEmailException(){
        return new ResponseEntity("The given email format is not valid!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NameTooShortException.class)
    public ResponseEntity handleNameTooShortException(){
        return new ResponseEntity("The name is too short!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NameTooLongException.class)
    public ResponseEntity handleNameTooLongException(){
        return new ResponseEntity("The name is too long!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SurnameTooShortException.class)
    public ResponseEntity handleSurnameTooShortException(){
        return new ResponseEntity("The surname is too short!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SurnameTooLongException.class)
    public ResponseEntity handleSurnameTooLongException(){
        return new ResponseEntity("The surname is too long!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongTokenException.class)
    public ResponseEntity handleWrongTokenException(){
        return new ResponseEntity("The received token is not valid!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullDtoFieldException.class)
    public ResponseEntity handleNullDtoFieldException(){
        return new ResponseEntity("A DTO field was null!", HttpStatus.BAD_REQUEST);
    }
}
