package com.agrilink.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {
    private String role;
    // Labourer fields
    private Long totalAvailableJobs;
    private Long totalApplicationsSent;
    private Long acceptedJobsCount;
    // Landowner fields
    private Long jobsPosted;
    private Long totalApplicationsReceived;
    private Long acceptedWorkersCount;
}