package com.example.javaspringlessons.dto;

import lombok.Data;

@Data
public class JwtRefreshRequest {
    private String refreshToken;
}
