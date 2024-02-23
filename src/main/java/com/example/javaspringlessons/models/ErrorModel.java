package com.example.javaspringlessons.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorModel {
    private String message;
    private Date time;
}
