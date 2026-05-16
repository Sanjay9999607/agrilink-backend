package com.agrilink.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private String name;
}