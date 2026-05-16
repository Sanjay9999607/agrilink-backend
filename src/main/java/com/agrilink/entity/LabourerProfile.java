package com.agrilink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "labourer_profiles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LabourerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String skills;
}