package com.agrilink.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

    @NotNull
    private Long jobId;

    @NotNull
    private Long revieweeId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
