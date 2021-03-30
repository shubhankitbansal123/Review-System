package com.example.controller;

import com.example.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<Object> DataNotFoundException(DataNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DataAlreadyExistException.class)
    public ResponseEntity<Object> DataAlreadyExistException(DataAlreadyExistException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = SignUpNotRequiredException.class)
    public ResponseEntity<Object> SignUpNotRequiredException(SignUpNotRequiredException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = WrongInformationException.class)
    public ResponseEntity<Object> WrongInformationException(WrongInformationException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = GenericException.class)
    public ResponseEntity<Object> GenericException(GenericException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicateEntryException.class)
    public ResponseEntity<Object> DuplicateEntryException(DuplicateEntryException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ParameterMissingException.class)
    public ResponseEntity<Object> ParameterMissingException(ParameterMissingException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
