package com.agrilink.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String payment;

    @Column(nullable = false)
    private Integer workersRequired;

    @Builder.Default
    private Integer workersAcceptedCount = 0;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    // Landowner sets this — labourers beyond this km cannot see job
    @Column(nullable = false)
    private Double maxDistance;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "landowner_id", nullable = false)
    private User landowner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum JobStatus { OPEN, CLOSED }
}