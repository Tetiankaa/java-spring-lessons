package com.example.javaspringlessons.models;

import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;
}
