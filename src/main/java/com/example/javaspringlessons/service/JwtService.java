package com.example.javaspringlessons.service;

import com.example.javaspringlessons.entity.User;
import com.example.javaspringlessons.repository.UserDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserDAO userDAO;


    @Value("${jwt.signinKey}")
    private String secrete_key;

    public String generateAccessToken(UserDetails userDetails){
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .setClaims(Map.of("roles",roles))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 20))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){

        return Jwts
                .builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .setSubject(userDetails.getUsername())
                .signWith(getSigninKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String token){
        return resolveClaims(Claims::getExpiration,token).before(new Date());
    }

    public String extractUsername(String token){
        return resolveClaims(Claims::getSubject, token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
       return (extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isRefreshToken(String token, String username){
        User user = userDAO.findByEmail(username).orElseThrow();
        return token.equals(user.getRefreshToken());
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private <T> T resolveClaims(Function<Claims,T> resolve, String token){
        Claims claims = extractAllClaims(token);
        return resolve.apply(claims);
    }

    private Key getSigninKey(){
        byte[] decodedSecretKey = Decoders.BASE64.decode(secrete_key);
        return Keys.hmacShaKeyFor(decodedSecretKey);
    }
}
