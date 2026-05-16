package com.agrilink.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String payment;
    private Integer workersRequired;
    private Integer workersAcceptedCount;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private Double maxDistance;
    private String status;
    private String landownerName;
    private Double distanceKm;
    private LocalDateTime createdAt;
}