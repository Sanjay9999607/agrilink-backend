package com.agrilink.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Payment is required")
    private String payment;

    @NotNull @Positive
    private Integer workersRequired;

    @NotBlank(message = "Location is required")
    private String location;

    // Sent by frontend after geocoding in browser
    private Double latitude;
    private Double longitude;

    // Landowner sets this — max km labourers can be
    @NotNull @Positive
    private Double maxDistance;
}