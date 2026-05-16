package com.agrilink.service;

import com.agrilink.dto.*;
import com.agrilink.entity.*;
import com.agrilink.repository.*;
import com.agrilink.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final LabourerProfileRepository labRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthResponse register(RegisterRequest req) {

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException(
                "Email already registered");
        }

        // Coordinates come from frontend Photon geocoding
        // Backend does NOT call any geocoding API
        if (req.getLatitude() == null
                || req.getLongitude() == null) {
            throw new RuntimeException(
                "Location coordinates missing. "
                + "Please wait for GPS to resolve "
                + "before submitting.");
        }

        User user = User.builder()
            .name(req.getName())
            .email(req.getEmail())
            .password(encoder.encode(req.getPassword()))
            .phone(req.getPhone())
            .role(req.getRole())
            .locationName(req.getLocation())
            .latitude(req.getLatitude())
            .longitude(req.getLongitude())
            .workRadius(req.getWorkRadius())
            .build();

        userRepo.save(user);

        if (req.getRole() == User.Role.LABOURER) {
            LabourerProfile profile = LabourerProfile.builder()
                .user(user)
                .skills(req.getSkills() != null
                    ? req.getSkills() : "")
                .build();
            labRepo.save(profile);
        }

        String token = jwtUtil.generateToken(
            user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .userId(user.getId())
            .name(user.getName())
            .build();
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepo.findByEmail(req.getEmail())
            .orElseThrow(() ->
                new RuntimeException("User not found"));

        if (!encoder.matches(
                req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtUtil.generateToken(
            user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .userId(user.getId())
            .name(user.getName())
            .build();
    }
}