package com.example.javaspringlessons.filters;

import com.example.javaspringlessons.error.JwtAuthException;
import com.example.javaspringlessons.handler.AuthErrorHandler;
import com.example.javaspringlessons.service.JwtService;
import io.jsonwebtoken.JwtException;
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
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthErrorHandler authErrorHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(AUTHORIZATION_HEADER);

            if (authHeader == null || !authHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }

            String token = authHeader.substring(AUTHORIZATION_HEADER_PREFIX.length());

            String username = jwtService.extractUsername(token);

            if (jwtService.isRefreshToken(token,username)){
                response.setHeader("Error","Refresh token can not be used for accessing resources");
                throw new JwtException("Refresh token can not be used for accessing resources");
            }
            // if there is an authenticated user associated with the current thread of execution
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token,userDetails)){

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,userDetails.getPassword(), userDetails.getAuthorities()
                    );

                    WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                    authenticationToken.setDetails(webAuthenticationDetails);

                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(securityContext);
                }

            }
        }catch (JwtException exception){
            authErrorHandler.commence(request,response,new JwtAuthException(exception.getMessage(),exception));
        }

        filterChain.doFilter(request,response);
    }
}
