package com.example.javaspringlessons.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static long TOKEN_TTL_MILLIS = 60 * 1000;

    @Value("${jwt.signInKey}")
    private String signInKey;

    private Key key;

    @PostConstruct
    public void setUpKey(){
        key = Keys.hmacShaKeyFor(signInKey.getBytes(StandardCharsets.UTF_8)); // key is initialized using the value of signInKey.
    }

    public String generateToken(UserDetails userDetails){

        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        return  Jwts.builder()
                .addClaims(Map.of("roles",roles))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TTL_MILLIS))
                .signWith(key, SignatureAlgorithm.HS256) // token is signed using the HMAC-SHA256 algorithm with the cryptographic key
                .compact();

    }

    public boolean isTokenExpired(String token){
        return resolveClaim(token,Claims::getExpiration).before(new Date());
    }

    public String extractUsername(String token){
        return resolveClaim(token,Claims::getSubject);
    }

    private <T> T resolveClaim(String token, Function<Claims,T> resolver){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody();

        return resolver.apply(claims);

        // This method takes a JWT token and a resolver function as input.
        // It parses the token, extracts the claims payload, and then applies the resolver function to the claims object.
    }


}
