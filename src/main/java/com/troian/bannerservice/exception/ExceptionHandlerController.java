package com.troian.bannerservice.exception;

import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Log
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoBannerException.class)
    public ResponseEntity<ErrorMessage> authenticationExceptionHandler(NoBannerException exception) {

        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<ErrorMessage> responseStatusExceptionHandler(AccessException exception) {
        log.info("Catch Method Not Allowed");
        return new ResponseEntity<>(new ErrorMessage(exception, 405), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> duplicateExceptionHandler(DataIntegrityViolationException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("Duplicate entry");
        errorMessage.setCode(400);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> incorrectCategoryIdForAddingToBannerExceptionHandler(NullPointerException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        if(exception.getLocalizedMessage()!= null || exception.getLocalizedMessage().contains("category")){
            errorMessage.setMessage("Incorrect category Id");
            errorMessage.setCode(400);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorMessage(exception, 500), HttpStatusCode.valueOf(500));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        String defaultMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        errorMessage.setMessage(defaultMessage);
        errorMessage.setCode(400);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> allOtherExceptionHandler(Exception exception) {
        log.info("Catch other exception");
        return new ResponseEntity<>(new ErrorMessage(exception, 400), HttpStatus.BAD_REQUEST);
    }
}
