package com.rt_chatApp.controller;

import com.rt_chatApp.Dto.CustomExceptionDto;
import com.rt_chatApp.Exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomExceptionDto> handleJwtExpirationException(
            TokenExpiredException e, HttpServletRequest request
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(401)
                .message("Session expired.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionDto> globalExceptionHandler(
            Exception e, HttpServletRequest request
    ){
        var response = CustomExceptionDto.builder()
                .date(new Date())
                .statusCode(500)
                .message("Internal Server Error")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
