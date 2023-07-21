package com.indi.project.exception;

import com.indi.project.dto.exception.ErrorDto;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handlerException(IllegalArgumentException e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ErrorDto> handlerException(IllegalStateException e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ErrorDto> handlerException(
            InvalidDataAccessApiUsageException e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ErrorDto> handlerException(
            NullPointerException e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
