package com.example.javaspringlessons.config;

import com.example.javaspringlessons.handler.AuthErrorHandler;
import com.example.javaspringlessons.security.JwtAuthFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;
import java.util.Map;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

        private final AuthErrorHandler authErrorHandler;

        @Value("${auth.issuers.microsoft}")
        private String microsoftIssuer;

        @Value("${auth.issuers.google}")
        private String googleIssuer;

        private final ObjectMapper objectMapper;


        private Map<String,JwtDecoder> decoders;

    public SecurityConfig(AuthErrorHandler authErrorHandler, ObjectMapper objectMapper) {
        this.authErrorHandler = authErrorHandler;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
        public void init(){
            decoders = Map.of(
                    googleIssuer, JwtDecoders.fromIssuerLocation(googleIssuer),
                    microsoftIssuer, JwtDecoders.fromIssuerLocation(microsoftIssuer)
            );
        }
        @Bean
        public JwtDecoder multiTenancyJwtDecoder(){
            return token -> {
                String[] parts =  token.split("\\.");

                if (parts.length <=2){
                    throw new JwtException("Invalid Jwt");
                }

                byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
                System.out.println(decodedBytes);
                String payload = new String(decodedBytes);
                System.out.println(payload);

                try {
                    Map<String, Object> claims = objectMapper.readValue(payload, new TypeReference<>() {
                    });
                    System.out.println(claims);
                    String issuer = String.valueOf(claims.get("iss"));

                    JwtDecoder jwtDecoder = decoders.get(issuer);

                    if (jwtDecoder == null){
                        throw new io.jsonwebtoken.JwtException("Unable to decode JWT. Issuer not found or not supported.");
                    }

                    Jwt jwt = jwtDecoder.decode(token);
                    return jwt;


                }catch (JsonProcessingException exception){
                    throw new io.jsonwebtoken.JwtException("Invalid JWT payload",exception);
                }

            };

        }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        inMemoryUserDetailsManager.createUser(
                User.builder()
                        .username("buyer_userr")
                        .password(passwordEncoder().encode("123qwe"))
                        .roles("BUYER")
                        .build());

        inMemoryUserDetailsManager.createUser(
                User.builder()
                        .username("seller_user")
                        .password(passwordEncoder().encode("789456"))
                        .roles("SELLER")
                        .build());

        return inMemoryUserDetailsManager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider, JwtAuthFilter jwtAuthFilter) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**","/error").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2configurer->oauth2configurer.jwt(jwtConfigurer -> jwtConfigurer.decoder(multiTenancyJwtDecoder())))

//                .oauth2ResourceServer(oauth2configurer -> oauth2configurer.jwt(Customizer.withDefaults()))

//                .authenticationProvider(authenticationProvider)
//                //custom authentication logic(jwtAuthFilter) is executed before the default username/password authentication logic(UsernamePasswordAuthenticationFilter)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(configurer->configurer.authenticationEntryPoint(authErrorHandler))

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
      return configuration.getAuthenticationManager();
    }
}