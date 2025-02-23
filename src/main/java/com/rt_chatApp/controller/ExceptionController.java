package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.CustomExceptionDto;
import com.rt_chatApp.Exceptions.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<?> handleCompletionException(
            CompletionException e
    ) {
        Throwable cause = e.getCause();

        if (cause instanceof UserNotFoundException) {
            return handleUserNotFoundException((UserNotFoundException) cause);
        } else if (cause instanceof IllegalStateException) {
            return handleIllegalStateException((IllegalStateException) cause);
        }

        return globalExceptionHandler(e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomExceptionDto> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CustomExceptionDto> handleIllegalStateException(
            IllegalStateException e
    ) {
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<CustomExceptionDto> handleAuthorizationDeniedException(
            AuthorizationDeniedException e
    ) {
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomExceptionDto> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomExceptionDto> handleJwtExpirationException(
            ExpiredJwtException e
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("Session expired.")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<CustomExceptionDto> handleMalformedJwtException(
            MalformedJwtException e
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Invalid JWT token.")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(
            BadCredentialsException ex
    ){
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionDto> globalExceptionHandler(
            Exception e
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
