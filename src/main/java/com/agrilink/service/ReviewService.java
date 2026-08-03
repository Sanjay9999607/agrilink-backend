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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final JobRepository jobRepo;
    private final ApplicationRepository applicationRepo;

    public ReviewResponse createReview(CreateReviewRequest req, String email) {
        User reviewer = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        User reviewee = userRepo.findById(req.getRevieweeId()).orElseThrow(() -> new RuntimeException("Reviewee not found"));
        Job job = jobRepo.findById(req.getJobId()).orElseThrow(() -> new RuntimeException("Job not found"));

        boolean isLandowner = job.getLandowner().getId().equals(reviewer.getId());
        if (isLandowner) {
            Application application = applicationRepo.findByJobAndLabourer(job, reviewee)
                    .orElseThrow(() -> new RuntimeException("This labourer is not part of this job"));
            if (application.getStatus() != Application.AppStatus.ACCEPTED) {
                throw new RuntimeException("You can only review labourers with accepted applications");
            }
        } else {
            if (!job.getLandowner().getId().equals(reviewee.getId())) {
                throw new RuntimeException("Invalid reviewee for this job");
            }
            Application application = applicationRepo.findByJobAndLabourer(job, reviewer)
                    .orElseThrow(() -> new RuntimeException("You are not part of this job"));
            if (application.getStatus() != Application.AppStatus.ACCEPTED) {
                throw new RuntimeException("You can only review accepted applications");
            }
        }

        if (reviewRepo.existsByJobAndReviewerAndReviewee(job, reviewer, reviewee)) {
            throw new RuntimeException("You already reviewed this user for this job");
        }

        Review review = Review.builder()
                .job(job)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .rating(req.getRating())
                .comment(req.getComment())
                .build();

        Review saved = reviewRepo.save(review);
        updateUserRating(reviewee);
        return toResponse(saved);
    }

    public List<ReviewResponse> getReviewsForUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepo.findByReviewee(user).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ReviewResponse> getMyReviews(String email) {
        User reviewer = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepo.findByReviewer(reviewer).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .jobId(review.getJob().getId())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getName())
                .revieweeId(review.getReviewee().getId())
                .revieweeName(review.getReviewee().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private void updateUserRating(User reviewee) {
        List<Review> reviews = reviewRepo.findByReviewee(reviewee);
        double avg = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
        reviewee.setAverageRating(avg);
        reviewee.setRatingCount((long) reviews.size());
        userRepo.save(reviewee);
    }
}
