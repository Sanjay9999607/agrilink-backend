package com.agrilink.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "labourer_id", nullable = false)
    private User labourer;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AppStatus status = AppStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime appliedAt;

    public enum AppStatus { PENDING, ACCEPTED, REJECTED }
}