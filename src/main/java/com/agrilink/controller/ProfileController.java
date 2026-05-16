package com.agrilink.controller;

import com.agrilink.dto.*;
import com.agrilink.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context
    .SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> get() {
        return ResponseEntity.ok(
            profileService.getProfile(email()));
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> update(
            @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(
            profileService.updateProfile(email(), req));
    }

    private String email() {
        return SecurityContextHolder.getContext()
            .getAuthentication().getName();
    }
}