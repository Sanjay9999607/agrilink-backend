package com.agrilink.config;

import com.agrilink.repository.UserRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web
    .builders.HttpSecurity;
import org.springframework.security.config.annotation.web
    .configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web
    .configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http
    .SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt
    .BCryptPasswordEncoder;
import org.springframework.security.crypto.password
    .PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication
    .UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserRepository userRepo;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepo.findByEmail(email)
            .map(u -> User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRole().name())
                .build())
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "User not found"));
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes(
                    "Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}