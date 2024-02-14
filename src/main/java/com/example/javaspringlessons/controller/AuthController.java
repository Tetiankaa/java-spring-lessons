package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.dto.JwtRequest;
import com.example.javaspringlessons.dto.JwtResponse;
import com.example.javaspringlessons.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/api/auth/signin")
    public ResponseEntity<JwtResponse> signIn (@RequestBody JwtRequest jwtRequest){

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(jwtRequest.getUsername(), jwtRequest.getPassword());
        authenticationManager.authenticate(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtService.generateToken(userDetails);

        JwtResponse response = new JwtResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

}
