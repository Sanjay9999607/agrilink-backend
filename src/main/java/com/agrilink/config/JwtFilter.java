package com.agrilink.config;

import com.agrilink.repository.UserRepository;
import com.agrilink.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication
    .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority
    .SimpleGrantedAuthority;
import org.springframework.security.core.context
    .SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null
                && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = jwtUtil.extractEmail(token);

                if (email != null
                        && SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                    userRepo.findByEmail(email).ifPresent(user -> {
                        var auth =
                            new UsernamePasswordAuthenticationToken(
                                email, null,
                                List.of(new SimpleGrantedAuthority(
                                    "ROLE_" + user.getRole().name()))
                            );
                        SecurityContextHolder.getContext()
                            .setAuthentication(auth);
                    });
                }
            } catch (Exception e) {
                System.out.println(
                    "JWT error: " + e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }
}