package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public ResponseEntity<String> handleException(SQLIntegrityConstraintViolationException e){
//        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleDatabaseException(SQLIntegrityConstraintViolationException e){
       return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(ErrorDTO.builder()
                       .timestamp(System.currentTimeMillis())
                       .details(e.getMessage())
                       .build()
               );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException exception){

        BindingResult bindingResult = exception.getBindingResult();

        List<String> validationErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    if (fieldError.getCodes() != null) {
                        for (String code : fieldError.getCodes()) {
                            if (code.startsWith("Min") || code.startsWith("Max")) {

                                return fieldError.getDefaultMessage();
                            }
                        }
                    }
                    return fieldError.getDefaultMessage();
                })
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO.builder()
                        .timestamp(System.currentTimeMillis())
                        .details(validationErrors.toString())
                        .build());
    }
}
