package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.dto.JwtRefreshRequest;
import com.example.javaspringlessons.dto.JwtRequest;
import com.example.javaspringlessons.dto.JwtResponse;
import com.example.javaspringlessons.exception.JwtAuthException;
import com.example.javaspringlessons.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static long ACCESS_TOKEN_TTL_SECONDS = 30000;
    private static long REFRESH_TOKEN_TTL_SECONDS = 90000;


    @PostMapping("/api/auth/signin")
    public ResponseEntity<JwtResponse> signIn (@RequestBody JwtRequest jwtRequest){
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(jwtRequest.getUsername(), jwtRequest.getPassword());
        authenticationManager.authenticate(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        String accessToken = jwtService.generateAccessToken(userDetails,ACCESS_TOKEN_TTL_SECONDS);
        String refreshToken = jwtService.generateRefreshToken(userDetails,REFRESH_TOKEN_TTL_SECONDS);

        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

            return ResponseEntity.ok(response);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody @Valid JwtRefreshRequest jwtRefreshRequest) {
        try {
            Date refreshTokenExpirationDate = jwtService.getExpiration(jwtRefreshRequest.getRefreshToken());

            if (refreshTokenExpirationDate.before(new Date())) {
                throw new JwtAuthException("Refresh token has expired");
            }

            String username = jwtService.extractUsername(jwtRefreshRequest.getRefreshToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String accessToken = jwtService.generateAccessToken(userDetails, ACCESS_TOKEN_TTL_SECONDS);
            Date accessTokenExpirationDate = jwtService.getExpiration(accessToken);

            JwtResponse response = new JwtResponse();

            response.setAccessToken(accessToken);

            if (refreshTokenExpirationDate.before(accessTokenExpirationDate)) {
                String refreshToken = jwtService.generateRefreshToken(userDetails, REFRESH_TOKEN_TTL_SECONDS);
                response.setRefreshToken(refreshToken);
            } else {
                response.setRefreshToken(jwtRefreshRequest.getRefreshToken());
            }

            return ResponseEntity.ok(response);
        } catch (JwtException exception) {
            throw new JwtAuthException(exception.getMessage(), exception);
        }
    }

}
