package com.jeferson.conspre.controllers.handlers;

import com.jeferson.conspre.services.exceptions.DatabaseException;
import com.jeferson.conspre.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ValidationError> handlerResourceNotFound(ResourceNotFoundException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;

        ValidationError error = new ValidationError();
        error.setTimeStamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Not Found Exception");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ValidationError> handlerDatabaseException(DatabaseException e, HttpServletRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ValidationError error = new ValidationError();
        error.setTimeStamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Violação de integridade do banco de dados");
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }
}
