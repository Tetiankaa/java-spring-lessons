package com.example.javaspringlessons.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
