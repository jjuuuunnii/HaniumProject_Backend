package com.indi.project.exception;

import com.indi.project.dto.exception.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorDto> handlerException(IllegalArgumentException e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
