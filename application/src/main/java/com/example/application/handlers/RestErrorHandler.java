package com.example.conveyor.handlers;

import com.example.conveyor.dto.ErrorDTO;
import com.example.conveyor.exceptions.AgeException;
import com.example.conveyor.exceptions.CreditException;
import com.example.conveyor.exceptions.ScoringException;
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

    @ExceptionHandler(AgeException.class)
    public ResponseEntity<ErrorDTO> handleException(AgeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<ErrorDTO> handleException(ScoringException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CreditException.class)
    public ResponseEntity<ErrorDTO> handleException(CreditException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        FieldError fieldError = exception.getBindingResult().getFieldError();
        log.error("Error validation, field:{}, {}", fieldError.getField(), fieldError.getDefaultMessage());
        String message = "Error validation, field " + fieldError.getField() + ", " + fieldError.getDefaultMessage();
        return new ResponseEntity<>(new ErrorDTO(message), status);
    }
}
