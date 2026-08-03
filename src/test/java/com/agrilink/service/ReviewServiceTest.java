package com.agrilink.service;

import com.agrilink.dto.CreateReviewRequest;
import com.agrilink.dto.ReviewResponse;
import com.agrilink.entity.Application;
import com.agrilink.entity.Job;
import com.agrilink.entity.Review;
import com.agrilink.entity.User;
import com.agrilink.repository.ApplicationRepository;
import com.agrilink.repository.JobRepository;
import com.agrilink.repository.ReviewRepository;
import com.agrilink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private JobRepository jobRepo;

    @Mock
    private ApplicationRepository applicationRepo;

    @Mock
    private ReviewRepository reviewRepo;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateReviewWhenAcceptedApplicationExists() {
        User reviewer = User.builder()
                .id(1L)
                .email("reviewer@test.com")
                .role(User.Role.LABOURER)
                .build();

        User reviewee = User.builder()
                .id(2L)
                .email("reviewee@test.com")
                .role(User.Role.LANDOWNER)
                .build();

        Job job = Job.builder()
                .id(10L)
                .landowner(reviewee)
                .status(Job.JobStatus.CLOSED)
                .build();

        Application acceptedApplication = Application.builder()
                .job(job)
                .labourer(reviewer)
                .status(Application.AppStatus.ACCEPTED)
                .build();

        when(userRepo.findByEmail("reviewer@test.com")).thenReturn(Optional.of(reviewer));
        when(userRepo.findById(2L)).thenReturn(Optional.of(reviewee));
        when(jobRepo.findById(10L)).thenReturn(Optional.of(job));
        when(applicationRepo.findByJobAndLabourer(job, reviewer)).thenReturn(Optional.of(acceptedApplication));
        when(reviewRepo.existsByJobAndReviewerAndReviewee(job, reviewer, reviewee)).thenReturn(false);
        when(reviewRepo.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponse response = reviewService.createReview(
                new CreateReviewRequest(10L, 2L, 5, "Great work"),
                "reviewer@test.com"
        );

        assertEquals(5, response.getRating());
        assertEquals("Great work", response.getComment());
    }
}
