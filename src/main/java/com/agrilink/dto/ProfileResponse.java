package com.agrilink.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private String skills;
    private Double workRadius;
    private Double averageRating;
    private Long ratingCount;
}