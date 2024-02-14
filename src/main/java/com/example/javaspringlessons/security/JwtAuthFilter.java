package com.example.javaspringlessons.security;

import com.example.javaspringlessons.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authorization) && !StringUtils.startsWithIgnoreCase(authorization,AUTHORIZATION_HEADER_PREFIX)){
            filterChain.doFilter(request,response);
            return;
        }

        String token = authorization.substring(AUTHORIZATION_HEADER.length());

        if (jwtService.isTokenExpired(token)){
            filterChain.doFilter(request,response);
            return;
        }

        String username = jwtService.extractUsername(token);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        
        if (StringUtils.hasText(username) && securityContext.getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(username,userDetails.getPassword(),userDetails.getAuthorities());
            securityContext.setAuthentication(authentication);

        }
        filterChain.doFilter(request,response);
        
    }
}
