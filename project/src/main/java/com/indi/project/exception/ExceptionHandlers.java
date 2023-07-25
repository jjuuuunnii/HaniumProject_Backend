package com.indi.project.exception;

import com.indi.project.dto.exception.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

/*    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handlerException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ErrorDto> handlerException(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ErrorDto> handlerException(
            InvalidDataAccessApiUsageException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ErrorDto> handlerException(
            NullPointerException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return new ResponseEntity<>(new ErrorDto(errorMessage), HttpStatus.BAD_REQUEST);
    }*/


    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorDto> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException e){
        ErrorCode ex = ErrorCode.AUTHENTICATION_ERROR_NO_USER;
        log.error("error={}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(ex.getStatus(), ex.getCode(),
                ex.getDescription()), HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorDto> handleInternalAuthenticationServiceException(
            UsernameNotFoundException e){
        ErrorCode ex = ErrorCode.AUTHENTICATION_ERROR_NO_USER;
        log.error("error={}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(ex.getStatus(), ex.getCode(),
                ex.getDescription()), HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
        log.info("error={}",e.getErrorCode().getCode());
        return new ResponseEntity<>(new ErrorDto(e.getErrorCode().getStatus(), e.getErrorCode().getCode(),
                e.getErrorCode().getDescription()), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

}
