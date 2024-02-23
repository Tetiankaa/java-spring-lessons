package com.example.javaspringlessons.service;

import com.example.javaspringlessons.models.AuthRequest;
import com.example.javaspringlessons.models.AuthResponse;
import com.example.javaspringlessons.models.RefreshRequest;
import com.example.javaspringlessons.models.RegisterRequest;
import com.example.javaspringlessons.entity.Roles;
import com.example.javaspringlessons.entity.User;
import com.example.javaspringlessons.repository.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDAO userDAO;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest userToRegister){

        User userToSave = User.builder()
                .email(userToRegister.getEmail())
                .password(passwordEncoder.encode(userToRegister.getPassword()))
                .firstName(userToRegister.getFirstName())
                .lastName(userToRegister.getLastName())
                .role(Roles.USER)
                .build();

        String accessToken = jwtService.generateAccessToken(userToSave);
        String refreshToken = jwtService.generateRefreshToken(userToSave);

        userToSave.setRefreshToken(refreshToken);

        userDAO.save(userToSave);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);

        return authResponse;

    }

    public AuthResponse authenticate(AuthRequest authRequest){
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        authenticationManager.authenticate(authentication);

        User user = userDAO.findByEmail(authRequest.getEmail()).orElseThrow();


        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);

        userDAO.save(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);

        return authResponse;
    }

    public AuthResponse refresh(RefreshRequest refreshToken) {
        String username = jwtService.extractUsername(refreshToken.getRefresh());

        User user = userDAO.findByEmail(username).orElseThrow();
        String newAccessToken = null;
        String newRefreshToken = null;
        if (refreshToken.getRefresh().equals(user.getRefreshToken())) {

            newAccessToken = jwtService.generateAccessToken(user);
            newRefreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(newRefreshToken);

            userDAO.save(user);

        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(newAccessToken);
        authResponse.setRefreshToken(newRefreshToken);

        return authResponse;
    }
}
