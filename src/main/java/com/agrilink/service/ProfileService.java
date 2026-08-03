package com.agrilink.service;

import com.agrilink.dto.*;
import com.agrilink.entity.*;
import com.agrilink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepo;
    private final LabourerProfileRepository labRepo;

    public ProfileResponse getProfile(String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow();
        String skills = null;
        if (user.getRole() == User.Role.LABOURER) {
            skills = labRepo.findByUser(user)
                .map(LabourerProfile::getSkills)
                .orElse("");
        }
        return toResponse(user, skills);
    }

    public ProfileResponse updateProfile(
            String email, UpdateProfileRequest req) {

        User user = userRepo.findByEmail(email)
            .orElseThrow();

        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
        }

        // Location update — coordinates from frontend
        // Backend does NOT call any geocoding API
        if (req.getLocation() != null
                && !req.getLocation().isBlank()) {
            user.setLocationName(req.getLocation());
            if (req.getLatitude() != null
                    && req.getLongitude() != null) {
                user.setLatitude(req.getLatitude());
                user.setLongitude(req.getLongitude());
            }
        }

        if (req.getWorkRadius() != null) {
            user.setWorkRadius(req.getWorkRadius());
        }

        userRepo.save(user);

        String skills = null;
        if (user.getRole() == User.Role.LABOURER) {
            LabourerProfile profile =
                labRepo.findByUser(user).orElseThrow();
            if (req.getSkills() != null) {
                profile.setSkills(req.getSkills());
                labRepo.save(profile);
            }
            skills = profile.getSkills();
        }

        return toResponse(user, skills);
    }

    private ProfileResponse toResponse(
            User user, String skills) {
        return ProfileResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .role(user.getRole().name())
            .locationName(user.getLocationName())
            .latitude(user.getLatitude())
            .longitude(user.getLongitude())
            .skills(skills)
            .workRadius(user.getWorkRadius())
            .averageRating(user.getAverageRating())
            .ratingCount(user.getRatingCount())
            .build();
    }
}