package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.CustomExceptionDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

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
            ExpiredJwtException e, HttpServletRequest request
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(401)
                .message("Session expired.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<CustomExceptionDto> handleMalformedJwtException(
            MalformedJwtException e, HttpServletRequest request
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(400)
                .message("Invalid JWT token.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionDto> globalExceptionHandler(
            Exception e, HttpServletRequest request
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(500)
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
