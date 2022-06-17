package com.example.conveyor.handlers;

import com.example.conveyor.dto.Wrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Wrapper> handleException(Exception e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new Wrapper<>(e), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        FieldError fieldError = exception.getBindingResult().getFieldError();
        log.error("Error validation, field:{}, {}", fieldError.getField(), fieldError.getDefaultMessage());
        String bodyOfResponse = "Error validation, field " + fieldError.getField() + ", " + fieldError.getDefaultMessage();
        return new ResponseEntity<>(bodyOfResponse, status);
    }
}
