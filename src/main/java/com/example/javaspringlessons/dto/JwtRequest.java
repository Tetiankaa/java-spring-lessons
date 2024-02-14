package com.example.javaspringlessons.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class JwtRequest {
    private String password;
    private String username;
}
