package com.agrilink.dto;

import com.agrilink.entity.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password min 8 characters")
    private String password;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotNull(message = "Role is required")
    private User.Role role;

    @NotBlank(message = "Location is required")
    private String location;

    // Sent by frontend after geocoding in browser
    private Double latitude;
    private Double longitude;

    // LABOURER ONLY
    private String skills;
    private Double workRadius;
}