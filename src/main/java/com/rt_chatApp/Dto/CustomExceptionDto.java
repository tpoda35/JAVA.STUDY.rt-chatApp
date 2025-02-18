package com.rt_chatApp.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomExceptionDto{
    private Date date;
    private Integer statusCode;
    private String message;
    private String path;
}
