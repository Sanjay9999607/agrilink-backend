package com.agrilink.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String jobLocation;
    private String jobPayment;
    private Long labourerId;
    private String labourerName;
    private String labourerPhone;
    private String labourerEmail;
    private String labourerSkills;
    private String labourerLocation;
    private String landownerName;
    private String landownerPhone;
    private String landownerEmail;
    private String status;
    private LocalDateTime appliedAt;
}