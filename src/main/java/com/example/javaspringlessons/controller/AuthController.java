package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.dto.UserDTO;
import com.example.javaspringlessons.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/api/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO credentials){

       return ResponseEntity.ok(userService.save(credentials));
    }
}
