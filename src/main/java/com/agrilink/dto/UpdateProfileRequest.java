package com.agrilink.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String phone;
    private String location;
    private String skills;
    private Double workRadius;
    private Double latitude;
    private Double longitude;
}