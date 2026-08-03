package com.agrilink.controller;

import com.agrilink.dto.CreateReviewRequest;
import com.agrilink.dto.ReviewResponse;
import com.agrilink.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody CreateReviewRequest req) {
        return ResponseEntity.ok(reviewService.createReview(req, email()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        return ResponseEntity.ok(reviewService.getMyReviews(email()));
    }

    private String email() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
