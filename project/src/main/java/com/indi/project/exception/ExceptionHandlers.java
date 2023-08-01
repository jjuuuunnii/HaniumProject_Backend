package com.indi.project.exception;

import com.indi.project.success.Result;
import com.indi.project.dto.exception.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


/*    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorDto> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException e){
        ErrorCode ex = ErrorCode.AUTHENTICATION_ERROR_NO_USER;
        log.error("error={}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(ex.getStatus(), ex.getCode(),
                ex.getDescription()), HttpStatus.valueOf(ex.getStatus()));
    }*/

/*    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorDto> handleInternalAuthenticationServiceException(
            UsernameNotFoundException e){
        ErrorCode ex = ErrorCode.AUTHENTICATION_ERROR_NO_USER;
        log.error("error={}", e.getMessage());
        return new ResponseEntity<>(new ErrorDto(ex.getStatus(), ex.getCode(),
                ex.getDescription()), HttpStatus.valueOf(ex.getStatus()));
    }*/

    @ExceptionHandler({CustomException.class})
    public Result<ErrorDto> handleCustomException(CustomException e) {
        log.info("error = {}",e.getErrorCode().getCode());
        return new Result<>(new ErrorDto(e.getErrorCode().isSuccess(), e.getErrorCode().getCode()));
    }

    //UserJoinDto에서 null값이 들어왔을때의 오류
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new Result<>(new ErrorDto(false,"NULL POINT EXCEPTION"));
    }

}
